package com.payneteasy.websocket;

import java.util.Arrays;

import static com.payneteasy.websocket.WebSocketUtil.applyMask;
import static com.payneteasy.websocket.WebSocketUtil.fromBytesToText;

/**
 *
 */
public class WebSocketFrame {

    private WebSocketFrame(Builder aBuilder){
        fin = aBuilder.theFin;
        reserved = aBuilder.theReserved;
        opCode = aBuilder.theOpCode;
        maskedPayload = aBuilder.theMaskedPayload;
        payloadLength = aBuilder.thePayloadLength;
        maskingKey_4 = aBuilder.theMaskingKey_4;
        applicationData = aBuilder.theApplicationData;
    }

    // opcode
    public enum OpCode {

          CONTINUATION_FRAME   (0x00)
        , TEXT_FRAME           (0x01)
        , BINARY_FRAME         (0x02)
        , CONNECTION_CLOSE     (0x08)
        , PING                 (0x09)
        , PONG                 (0x10)
        ;

        private OpCode(int aCode) {
            code = (byte) aCode;
        }

        final byte code;
    }

    public final boolean                fin             ;
    public final int                    reserved        ;
    public final WebSocketFrame.OpCode  opCode          ;
    public final boolean                maskedPayload   ;
    public final int                    payloadLength   ;
    public final byte[]                 maskingKey_4    ;
    public final byte[]                 applicationData ;

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("{ ");
        sb.append(fin ? "final" : "not final");
        if(reserved>0) {
            sb.append(", reserved = ").append(reserved);
        }
        sb.append(", ").append(opCode);

        byte[] buf;
        if(maskedPayload && applicationData!=null) {
            sb.append(", masked ");
            buf = Arrays.copyOf(applicationData, applicationData.length);
            applyMask(maskingKey_4, buf);
        } else {
            buf = applicationData;
        }

        if(buf!=null) {
            sb.append(", '").append(fromBytesToText(buf, 0, buf.length)).append("'");
        }

        sb.append(" }");
        return sb.toString();
    }


    public static class Builder {

        public Builder fin(boolean aFin) {
            theFin = aFin;
            return this;
        }

        public Builder reserved(int aReserved) {
            theReserved = aReserved;
            return this;
        }

        public Builder opCode(OpCode aOpCode) {
            theOpCode = aOpCode;
            return this;
        }

        public Builder maskedPayload(boolean aMaskedPayload) {
            theMaskedPayload = aMaskedPayload;
            return this;
        }

        public Builder payloadLength(int aPayloadLength) {
            thePayloadLength = aPayloadLength;
            return this;
        }

        public Builder maskingKey_4(byte[] aMaskingKey_4) {
            theMaskingKey_4 = aMaskingKey_4;
            return this;
        }

        public Builder applicationData(byte[] aApplicationData) {
            theApplicationData = aApplicationData;
            return this;
        }

        public WebSocketFrame build() {
            return new WebSocketFrame(this);
        }

        private boolean theFin;
        private int theReserved;
        private OpCode theOpCode;
        private boolean theMaskedPayload;
        private int thePayloadLength;
        private byte[] theMaskingKey_4;
        private byte[] theApplicationData;

    }
}
