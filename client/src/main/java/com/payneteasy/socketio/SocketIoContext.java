package com.payneteasy.socketio;

/**
 *
 */
public class SocketIoContext {

    private final String id;
    private final SocketIoSession session;

    public SocketIoContext(String aId, SocketIoSession aSession) {
        id = aId;
        session = aSession;
    }

    public void ack() {
        if(id!=null) {
            session.sendAck(id);
        }
    }
}
