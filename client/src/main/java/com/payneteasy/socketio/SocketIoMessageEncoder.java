package com.payneteasy.socketio;

import com.payneteasy.websocket.WebSocketFrame;
import com.payneteasy.websocket.WebSocketFrameBuilder;
import com.payneteasy.websocket.WebSocketUtil;

/**
 *
 */
public class SocketIoMessageEncoder {

    public static final char DELIMITER = ':';

    public WebSocketFrame encode(SocketIoMessage aMessage) {
        StringBuilder sb = new StringBuilder();

        sb.append(aMessage.type.code);
        append(sb, aMessage.id);
        append(sb, aMessage.endPoint);

        if(WebSocketUtil.hasText(aMessage.data)) {
            sb.append(DELIMITER).append(aMessage.data);
        }

        return WebSocketFrameBuilder.createTextFrame(sb.toString());
    }

    private void append(StringBuilder sb, String aValue) {
        sb.append(DELIMITER);
        if(WebSocketUtil.hasText(aValue)) {
            sb.append(aValue);
        }
    }
}
