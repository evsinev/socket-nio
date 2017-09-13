package com.payneteasy.websocket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

/**
 *
 */
public class WebSocketWriterThread extends Thread {

    private static final Logger LOG = LoggerFactory.getLogger(WebSocketWriterThread.class);

    private final BufferedOutputStream out;
    private final IOutputQueue queue;
    private static final AtomicLong THREAD_ID = new AtomicLong();
    private final IWebSocketConfiguration config;

    public WebSocketWriterThread(IOutputQueue aQueue, OutputStream aOutput, IWebSocketConfiguration aConfig) {
        queue = aQueue;
        out = new BufferedOutputStream(aOutput, 1400);
        config = aConfig;
        setName("web-socket-writer-"+THREAD_ID.incrementAndGet());
    }

    @Override
    public void run() {

        WebSocketFrameEncoder encoder = new WebSocketFrameEncoder();

        while(!isInterrupted()) {
            try {
                WebSocketFrame frame;
                try {
                    frame = queue.nextFrame(config.getWriterNextFramePoolTimeout(), TimeUnit.MILLISECONDS);
                } catch (InterruptedException e) {
                    LOG.warn("Interrupted while getting next frame in writer thread, exiting ...");
                    break;
                }
                if(frame==null) {
                    continue;
                }

                try {
                    encoder.encode(frame, out);
                    out.flush();
                    LOG.debug("W-OUT: {}", frame);
                } catch (IOException e) {
                    LOG.error("W-OUT: error "+frame, e);
                    if(config.insertFrameAgainOnWriteError()) {
                        LOG.info("Reinsert frame again");
                        queue.insertFrameAgain(frame);
                    } else {
                        LOG.error("Skipping this frame to send. Use IWebSocketConfiguration.insertFrameAgainOnWriteError() to reinsert this frame again.");
                    }
                    closeOutputStream();
                    return;
                }
            } catch (Exception e) {
                // todo came up with this Exception
                LOG.error("Error sending frame", e);
            }
        }
        closeOutputStream();
    }

    private void closeOutputStream() {
        try {
            LOG.debug("Closing output stream");
            out.close();
        } catch (IOException e) {
            LOG.error("Unable to close output stream", e);
        }
    }
}
