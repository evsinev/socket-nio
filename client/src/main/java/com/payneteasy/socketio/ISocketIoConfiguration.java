package com.payneteasy.socketio;

/**
 *
 */
public interface ISocketIoConfiguration {

    long getHandshakeConnectionTimeout();
    long getHandshakeReadTimeout();

}
