package com.payneteasy.websocket;

/**
 *
 */
public interface IWebSocketConfiguration {

    long getConnectionTimeout();
    long getReadTimeout();
    long getWriterNextFramePoolTimeout();
}
