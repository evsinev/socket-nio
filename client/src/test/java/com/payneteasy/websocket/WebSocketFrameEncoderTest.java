package com.payneteasy.websocket;

import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 *
 */
public class WebSocketFrameEncoderTest {

    @Test
    public void encodeTestFrame() throws IOException {
        byte[] mask = HexUtil.parseHex("91 f5 6b 54");
        WebSocketFrame frame = WebSocketFrameBuilder.createTextFrame("6:::4+[]", mask);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        WebSocketFrameEncoder encoder = new WebSocketFrameEncoder();
        encoder.encode(frame, out);
        out.flush();

        byte[] buf = out.toByteArray();

        Assert.assertEquals(
                ("81 88 "
                + "91 f5 6b 54 " // mask
                + "a7 cf 51 6e a5 de 30 09").replaceAll(" ", "").toUpperCase() // payload
                , HexUtil.toHexString(buf));
    }
}
