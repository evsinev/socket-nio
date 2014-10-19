package com.payneteasy.socketio;

import com.payneteasy.websocket.*;
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
    private final SocketIoMessageEncoder encoder = new SocketIoMessageEncoder();

    public SocketIoProcessor(ISocketIoListener aListener) {
        listener = aListener;
    }

    @Override
    public void onMessage(MutableWebSocketFrame aFrame, WebSocketContext aContext) {
        SocketIoMessage message = decoder.decode(aFrame);
        LOG.debug("S-IN: {}", message);

        SocketIoContext ioContext = new SocketIoContext();

        switch (message.type) {
            case HEARTBEAT:
                sendHeartBeat(aContext);
                break;

            case EVENT:
                listener.onEvent(message.data, ioContext);
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

    private void sendConnectionClose(WebSocketContext aContext) {
        aContext.sendFrame(WebSocketFrameBuilder.createConnectionClose("got socket-io disconnect"));
    }

    private void sendHeartBeat(WebSocketContext aContext) {
        SocketIoMessage message = new SocketIoMessage();
        message.type = HEARTBEAT;
        send(message, aContext);

    }

    private void send(SocketIoMessage aMessage, WebSocketContext aContext) {
        LOG.debug("S-QUEUE: {}", aMessage);
        WebSocketFrame frame = encoder.encode(aMessage);
        aContext.sendFrame(frame);
    }

    @Override
    public void onFailure(Throwable aError) {
        listener.onFailure(aError);
    }
}
