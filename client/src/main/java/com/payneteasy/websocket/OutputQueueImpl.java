package com.payneteasy.websocket;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 *
 */
public class OutputQueueImpl implements IOutputQueue {
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
}
