package com.payneteasy.websocket;

import java.nio.charset.StandardCharsets;

import static com.payneteasy.websocket.WebSocketFrame.OpCode.CONNECTION_CLOSE;
import static com.payneteasy.websocket.WebSocketFrame.OpCode.TEXT_FRAME;
import static com.payneteasy.websocket.WebSocketUtil.applyMask;
import static com.payneteasy.websocket.WebSocketUtil.createMask;

/**
 *
 */
public class WebSocketFrameBuilder {


    public static WebSocketFrame createTextFrame(String aText) {
        byte[] mask = createMask();
        return createTextFrame(aText, mask);
    }

    public static WebSocketFrame createTextFrame(String aText, byte[] mask) {

        byte[] data = aText.getBytes(StandardCharsets.UTF_8);

        applyMask(mask, data);

        return new WebSocketFrame.Builder()
                .opCode(TEXT_FRAME)
                .fin(true)
                .maskedPayload(true)
                .maskingKey_4(mask)
                .payloadLength(data.length)
                .applicationData(data)
                .build();
    }

    public static WebSocketFrame createConnectionClose(String aReason) {
        byte[] data = WebSocketUtil.toBytes(aReason);
        byte[] mask = createMask();
        applyMask(mask, data);

        return new WebSocketFrame.Builder()
                .opCode(CONNECTION_CLOSE)
                .fin(true)
                .maskedPayload(true)
                .maskingKey_4(mask)
                .applicationData(data)
                .build();
    }
}
