package com.payneteasy.websocket;

/**
 *
 */
public interface IWebSocketConfiguration {

    long getConnectionTimeout();
    long getReadTimeout();
    long getWriterNextFramePoolTimeout();

    /**
     * If we can't write a current frame can we insert it to the tail of the queue to resend it
     * @return true - insert it again, false skip this frame to resend
     */
    boolean insertFrameAgainOnWriteError();
}
