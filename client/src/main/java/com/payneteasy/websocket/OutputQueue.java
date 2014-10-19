package com.payneteasy.websocket;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 *
 */
public class OutputQueue {
    private final ArrayBlockingQueue<WebSocketFrame> queue;

    public OutputQueue() {
        queue = new ArrayBlockingQueue<WebSocketFrame>(1024);
    }

    public void addFrame(WebSocketFrame aFrame) {
        queue.add(aFrame);
    }

    public WebSocketFrame nextFrame(long aTimeout, TimeUnit aUnit) throws InterruptedException {
        return queue.poll(aTimeout, aUnit);
    }
}
