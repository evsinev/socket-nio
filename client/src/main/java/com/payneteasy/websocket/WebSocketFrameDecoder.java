package com.payneteasy.websocket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.net.ProtocolException;

import static com.payneteasy.websocket.WebSocketFrame.OpCode.*;

/**
 *
 */
public class WebSocketFrameDecoder {

    private static final boolean DEBUG = false;

    private static final Logger LOG = LoggerFactory.getLogger(WebSocketFrameDecoder.class);

    byte[] lenBuffer = new byte[8];

    public void decode(InputStream aInputStream, MutableWebSocketFrame frame) throws IOException {
        int firstByte = WebSocketUtil.readByte(aInputStream);

        if(DEBUG) LOG.debug("First byte = {}", Integer.toHexString(firstByte));

        frame.fin      = (firstByte & 0x80) == 0x80;
        frame.reserved = (firstByte & 0x70) >> 4;
        frame.opCode   = decodeOpcode(firstByte);

        int secondByte = WebSocketUtil.readByte(aInputStream);
        if(DEBUG) LOG.debug("Second byte = {}", Integer.toHexString(secondByte));

        frame.maskedPayload = (secondByte & 0x80) == 0x80;
        frame.payloadLength = readPayloadLen(secondByte, aInputStream);

        if(DEBUG) LOG.debug("Length = {}", frame.payloadLength);

        if(frame.maskedPayload) {
            readFully(frame.maskingKey_4, 0, 4, aInputStream);
        }

        checkFrameSize(frame, frame.payloadLength);
        readFully(frame.applicationData, 0, frame.payloadLength, aInputStream);
    }

    private void checkFrameSize(MutableWebSocketFrame aFrame, int aLength) {
        // expand frame
        if(aFrame.applicationData.length < aLength) {
            LOG.debug("Expanding frame size to {}", aLength);
            aFrame.applicationData = new byte[aLength];
            return;
        }

        // shrink frame to default size
        if(aFrame.applicationData.length != MutableWebSocketFrame.BUF_LEN) {
            LOG.debug("Shrinking frame size to default {}", MutableWebSocketFrame.BUF_LEN);
            aFrame.applicationData = new byte[MutableWebSocketFrame.BUF_LEN];
        }
    }

    private int readPayloadLen(int aByte, InputStream aInputStream) throws IOException {
        int len1 = aByte & 0x7F;

        if(len1 >= 0 && len1 <= 125 ) {
            return len1;
        }

        if( len1 == 126) {
            return readUnsignedShort(aInputStream);
        }

        if( len1 == 127 ) {
            return readUnsignedLong(aInputStream);
        }

        throw new ProtocolException("Unknown len1 value " + len1);
    }

    private int readUnsignedLong(InputStream aInputStream) throws IOException {
        readFully(lenBuffer, 0, 8, aInputStream);

        // the first byte is the most significant
        // Big-endian
        long length = 0;
        for (byte b : lenBuffer) {
            length = (length << 8) + (b & 0xff);
        }

        if (length > Integer.MAX_VALUE) {
            throw new IllegalStateException("Payload size is to big (" + length + ")");
        }
        return (int)length;
    }

    private int readUnsignedShort(InputStream aInputStream) throws IOException {
        readFully(lenBuffer, 0, 2, aInputStream);

        return    (lenBuffer[0] & 0xff) << 8 | (lenBuffer[1] & 0xff) ;
    }

    private void readFully(byte[] aBuf, int aFrom, int aLength, InputStream aInputStream) throws IOException {

        int count = 0;
        do {
            count += aInputStream.read(aBuf, aFrom + count, aLength - count);

        } while (count < aLength);

        if(count != aLength) {
            throw new IllegalStateException("count < aLength not implemented yet. Request "+aLength+" but was "+count);
        }
    }

    private WebSocketFrame.OpCode decodeOpcode(int aFirstByte) {
        int code = aFirstByte & 0x0F;

        switch (code) {
            case 0x00: return CONTINUATION_FRAME;
            case 0x01: return TEXT_FRAME;
            case 0x02: return BINARY_FRAME;
            case 0x08: return CONNECTION_CLOSE;
            case 0x09: return PING;
            case 0x0A: return PONG;

            default:
                throw new IllegalStateException("Unsupported frame op-code: 0x" + Integer.toHexString(code));
        }
    }
}
