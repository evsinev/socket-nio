package com.payneteasy.websocket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 */
public class WebSocketContext {

    private static final Logger LOG = LoggerFactory.getLogger(WebSocketContext.class);

    private final IOutputQueue queue;

    public WebSocketContext(IOutputQueue queue) {
        this.queue = queue;
    }

    public void sendFrame(WebSocketFrame aFrame) {
        LOG.debug("W-QUEUE: {}", aFrame);
        queue.addFrame(aFrame);
    }
}
