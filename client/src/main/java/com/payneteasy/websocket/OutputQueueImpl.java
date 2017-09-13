package com.payneteasy.websocket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 *
 */
public class OutputQueueImpl implements IOutputQueue {
    private static final Logger LOG = LoggerFactory.getLogger(OutputQueueImpl.class);

    private final ArrayBlockingQueue<WebSocketFrame> queue;

    public OutputQueueImpl() {
        this(1024);
    }

    public OutputQueueImpl(int aSize) {
        queue = new ArrayBlockingQueue<WebSocketFrame>(aSize);
    }

    public void addFrame(WebSocketFrame aFrame) {
        queue.add(aFrame);
    }

    public WebSocketFrame nextFrame(long aTimeout, TimeUnit aUnit) throws InterruptedException {
        return queue.poll(aTimeout, aUnit);
    }

    @Override
    public void insertFrameAgain(WebSocketFrame aFrame) {
        try {
            queue.put(aFrame);
        } catch (InterruptedException e) {
            LOG.error("Could not insert frame at the tail, inserting to the beginning of the queue", e);
            queue.add(aFrame);
        }
    }
}
