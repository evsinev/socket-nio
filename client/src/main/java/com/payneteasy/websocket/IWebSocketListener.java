package com.payneteasy.websocket;

/**
 *
 */
public interface IWebSocketListener {

    void onMessage(MutableWebSocketFrame aFrame, WebSocketContext aContext);

    /**
     * todo do not used
     *
     * @param aError error
     */
    void onFailure(Throwable aError);
}
