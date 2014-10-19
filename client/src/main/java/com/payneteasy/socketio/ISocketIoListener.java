package com.payneteasy.socketio;

/**
 *
 */
public interface ISocketIoListener {

    void onEvent(Object aEvent, SocketIoContext aContext);

    void onFailure(Throwable aError);
}
