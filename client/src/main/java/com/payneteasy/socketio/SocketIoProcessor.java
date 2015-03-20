package com.payneteasy.socketio;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.payneteasy.socketio.json.IJsonConverter;
import com.payneteasy.websocket.IWebSocketListener;
import com.payneteasy.websocket.MutableWebSocketFrame;
import com.payneteasy.websocket.WebSocketContext;
import com.payneteasy.websocket.WebSocketFrameBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.payneteasy.socketio.SocketIoMessage.Type.HEARTBEAT;

/**
 *
 */
class SocketIoProcessor implements IWebSocketListener {

    private static final Logger LOG = LoggerFactory.getLogger(SocketIoProcessor.class);

    private final SocketIoMessageDecoder decoder = new SocketIoMessageDecoder();
    private final ISocketIoListener listener;
    private final IJsonConverter json;
    private final SocketIoSession session;

    public SocketIoProcessor(ISocketIoListener aListener, IJsonConverter aJson, SocketIoSession aSession) {
        listener = aListener;
        json = aJson;
        session = aSession;
    }

    @Override
    public void onMessage(MutableWebSocketFrame aFrame, WebSocketContext aContext) {
        SocketIoMessage message = decoder.decode(aFrame);
        if (LOG.isDebugEnabled()) {
            if (SocketIoMessage.Type.EVENT.equals(message.type) && message.data != null) {
                final JsonElement event = json.parse(message.data);
                LOG.debug("S-IN:\n{}", json.toPrettyJson(event));
            } else {
                LOG.debug("S-IN: {}", message);
            }
        }


        switch (message.type) {
            case HEARTBEAT:
                sendHeartBeat();
                break;

            case EVENT:
                processEvent(listener, message);
                break;

            case CONNECT:
                // just skip it
                break;

            case DISCONNECT:
                sendConnectionClose(aContext);
                break;

            // do nothing
            case NOOP:
                break;

            // not supported yet
            case ACK:
                break;

            default:
                LOG.error("Unknown message type: {}", message);
        }

    }

    private void processEvent(ISocketIoListener aListener, SocketIoMessage aMessage) {
//        SocketIoEvent event = json.fromJson(aMessage.data, SocketIoEvent.class);

        JsonElement event = json.parse(aMessage.data);
        Object[] argsArray;

        if (event instanceof JsonObject) {
            JsonObject object = (JsonObject)event;

            if (object.has("args")) {
                JsonArray args = object.getAsJsonArray("args");
                argsArray = new Object[args.size()];
                for (int i = 0; i < args.size(); i++) {
                    JsonElement e = args.get(i);
                    if (e != null && !e.isJsonNull()) {
                        argsArray[i] = args.get(i);
                    }
                }
            } else {
                argsArray = new Object[0];
            }

            String eventName = object.get("name").getAsString();

            SocketIoContext context = new SocketIoContext(aMessage.id, session);
            aListener.onEvent(eventName, context, argsArray);

        } else {
            LOG.error("Unknown json type: {}", event);
        }



    }

    private void sendConnectionClose(WebSocketContext aContext) {
        aContext.sendFrame(WebSocketFrameBuilder.createConnectionClose("Got server's socket-io disconnect"));
    }

    private void sendHeartBeat() {
        SocketIoMessage message = new SocketIoMessage.Builder()
                .type(HEARTBEAT)
                .build();
        send(message);
    }

    private void send(SocketIoMessage aMessage) {
        session.sendMessage(aMessage);
    }

    @Override
    public void onFailure(Throwable aError) {
        listener.onFailure(aError);
    }
}
