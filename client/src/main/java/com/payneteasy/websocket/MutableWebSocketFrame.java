package com.payneteasy.websocket;

import java.util.Arrays;

/**
 *
 */
public class MutableWebSocketFrame {

    public static final int BUF_LEN = 32 * 1024;

    public MutableWebSocketFrame() {
    }

    // Indicates that this is the final fragment in a message.  The first
    // fragment MAY also be the final fragment.
    boolean fin;

    int reserved;

    WebSocketFrame.OpCode opCode;

    boolean maskedPayload;

    int payloadLength;

    byte[] maskingKey_4 = new byte[4];

//    byte[] extensionData   = new byte[2048];
    byte[] applicationData = new byte[BUF_LEN];

    public String getTextData() {
         return new String(applicationData, 0, payloadLength);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("{ ");
        sb.append(fin ? "final" : "not final");
        if(reserved>0) {
            sb.append(", reserved = ").append(reserved);
        }
        sb.append(", ").append(opCode);
        if(maskedPayload) {

            sb.append(", masked (").append(Arrays.toString(maskingKey_4)).append(")");
        }
        sb.append(", payload = '").append(getTextData()).append("'");
        sb.append(" }");
        return sb.toString();
    }
}
