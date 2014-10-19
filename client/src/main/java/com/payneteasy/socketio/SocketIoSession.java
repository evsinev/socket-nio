package com.payneteasy.socketio;

import com.payneteasy.socketio.json.GsonConverter;
import com.payneteasy.socketio.json.IJsonConverter;
import com.payneteasy.websocket.WebSocketFrame;
import com.payneteasy.websocket.WebSocketSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 *
 */
public class SocketIoSession {

    private static final Logger LOG = LoggerFactory.getLogger(SocketIoSession.class);

    private final WebSocketSession webSession;
    private final IJsonConverter json = new GsonConverter();
    private final SocketIoMessageEncoder encoder = new SocketIoMessageEncoder();

    SocketIoSession(WebSocketSession aSession) {
        webSession = aSession;
    }

    public void startAndWait(ISocketIoListener aListener) throws IOException {
        webSession.startAndWait(new SocketIoProcessor(aListener, json, this));
    }

    public void close() throws IOException {
        webSession.close();
    }

    public void sendEvent(String aEventName, Object ... args) {

        SocketIoEvent event = new SocketIoEvent(aEventName, args);

        SocketIoMessage message = new SocketIoMessage.Builder()
                .type(SocketIoMessage.Type.EVENT)
                .data(json.toJson(event))
                .build();

        sendMessage(message);
    }

    void sendAck(String aId) {
        SocketIoMessage message = new SocketIoMessage.Builder()
                .type(SocketIoMessage.Type.ACK)
                .data(aId+"+[]")
                .build();
        sendMessage(message);
    }


    void sendMessage(SocketIoMessage aMessage) {
        LOG.debug("S-QUEUE: {}", aMessage);
        WebSocketFrame frame = encoder.encode(aMessage);
        webSession.send(frame);
    }

}
