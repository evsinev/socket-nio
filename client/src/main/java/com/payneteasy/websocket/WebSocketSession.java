package com.payneteasy.websocket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

/**
 *
 */
public class WebSocketSession {

    private static final Logger LOG = LoggerFactory.getLogger(WebSocketSession.class);

    private final WebSocketFrameDecoder frameDecoder = new WebSocketFrameDecoder();
    private final MutableWebSocketFrame frame = new MutableWebSocketFrame();
    private final OutputQueue queue = new OutputQueue();

    private final OutputStream out;
    private final InputStream in;
    private final Socket socket;
    private final IWebSocketConfiguration config;

    private volatile WebSocketWriterThread writerThread;

    WebSocketSession(Socket aSocket, OutputStream aOut, InputStream aInput, IWebSocketConfiguration aConfig) {
        out = aOut;
        in = aInput;
        socket = aSocket;
        config = aConfig;
    }

    public void startAndWait(IWebSocketListener aListener) throws IOException {

        writerThread = new WebSocketWriterThread(queue, out, aListener, config);
        writerThread.start();

        WebSocketContext context = new WebSocketContext(queue);

        try {
            while(!Thread.currentThread().isInterrupted()) {
                frameDecoder.decode(in, frame);
                LOG.debug("W-IN: {}", frame);
                aListener.onMessage(frame, context);
            }
        } catch (EOFException e) {
            LOG.debug("Connection closed");
        } finally {
            tryToClose();
        }

    }

    public void send(WebSocketFrame aFrame) {
        queue.addFrame(aFrame);
    }

    public void close() throws IOException {
        tryToClose();
    }

    private void tryToClose() throws IOException {
        // The below methods have synchronized blocks already

        if(!writerThread.isInterrupted()) {
            writerThread.interrupt();
        }

        if(!socket.isClosed()) {
            socket.close();
        }
    }
}
