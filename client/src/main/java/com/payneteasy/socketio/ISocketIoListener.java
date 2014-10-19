package com.payneteasy.socketio;

/**
 *
 */
public interface ISocketIoListener {

    void onEvent(String aEventName, SocketIoContext aContext, Object ... args);

    void onFailure(Throwable aError);
}
