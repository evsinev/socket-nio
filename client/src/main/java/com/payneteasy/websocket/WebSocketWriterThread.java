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
    private final OutputQueue queue;
    private static final AtomicLong THREAD_ID = new AtomicLong();
    private final IWebSocketConfiguration config;

    public WebSocketWriterThread(OutputQueue aQueue, OutputStream aOutput, IWebSocketListener aListener, IWebSocketConfiguration aConfig) {
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
                WebSocketFrame frame = queue.nextFrame(config.getWriterNextFramePoolTimeout(), TimeUnit.MILLISECONDS);
                if(frame==null) {
                    continue;
                }

                try {
                    encoder.encode(frame, out);
                    out.flush();
                    LOG.debug("W-OUT: {}", frame);
                } catch (IOException e) {
                    // todo close and notify
                    LOG.error("W-OUT: error "+frame, e);
                    break;
                }
            } catch (InterruptedException e) {
                LOG.warn("Thread is interrupted", e);
                break;
            } catch (Exception e) {
                // todo
                LOG.error("Error sending frame", e);
            }

        }

        try {
            out.close();
        } catch (IOException e) {
            LOG.error("Unable to close output", e);
        }

        LOG.debug("Exited from thread.");
    }
}
