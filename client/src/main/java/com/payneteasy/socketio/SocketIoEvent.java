package com.payneteasy.socketio;

/**
 *
 */
public class SocketIoEvent {

    public SocketIoEvent(String aName, Object ... aArgs) {
        name = aName;
        args = aArgs;
    }

    public final String name;
    public final Object[] args;
}
