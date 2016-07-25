package com.payneteasy.websocket;

import java.util.concurrent.TimeUnit;

public interface IOutputQueue {

    void addFrame(WebSocketFrame aFrame);

    WebSocketFrame nextFrame(long aTimeout, TimeUnit aUnit) throws InterruptedException;

}