package com.payneteasy.websocket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.net.ProtocolException;

import static com.payneteasy.websocket.WebSocketUtil.readByte;

/**
 *
 */
public class HttpResponseDecoder {

    private static final Logger LOG = LoggerFactory.getLogger(HttpResponseDecoder.class);
    private static final int BUF_LEN = 2048;

    private final byte[] buf = new byte[BUF_LEN];

    public void decode(InputStream aInput) throws IOException {

        String responseLine = readLine(aInput);
        if(!"HTTP/1.1 101 Switching Protocols".equals(responseLine)) {
            throw new ProtocolException("Bad status line: '"+responseLine+"'");
        }

        for(String header; !(header = readLine(aInput)).isEmpty(); ) {

            LOG.debug("Header: {}", header);
        }
    }

    private String readLine(InputStream aInput) throws IOException {
        for(int i=0; i< BUF_LEN; i++) {
            byte b  = (byte) readByte(aInput);
            buf[i] = b;
            if(b == '\n') {
                return new String(buf, 0, i-1);
            }
        }
        throw new IllegalStateException("Line must be less than "+BUF_LEN);
    }

}
