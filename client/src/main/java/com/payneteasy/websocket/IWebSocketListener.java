package com.payneteasy.websocket;

/**
 *
 */
public interface IWebSocketListener {

    void onMessage(MutableWebSocketFrame aFrame, WebSocketContext aContext);

    void onFailure(Throwable aError);
}
