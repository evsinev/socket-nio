package com.payneteasy.websocket;

import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.Random;

/**
 *
 */
public class WebSocketUtil {

    public static final Charset UTF_8 = Charset.forName("UTF-8");

    private static ThreadLocal<Random> randomHolder = new ThreadLocal<Random>() {
        @Override
        protected Random initialValue() {
            return new Random();
        }
    };

    public static int readByte(InputStream aInputStream) throws IOException {
        int b = aInputStream.read();
        if (b == -1) {
            throw new EOFException("InputStream closed");
        }
        return b;
    }

    public static boolean hasText(String aText) {
        return !isEmpty(aText);
    }

    public static boolean isEmpty(String aText) {
        return aText == null || aText.isEmpty() || aText.trim().isEmpty();
    }

    public static String fromBytesToText(byte[] aBuf, int aOffset, int aLength) {
        return new String(aBuf, aOffset, aLength);
    }

    public static String toHex(byte len1) {
        return "0x"+Integer.toHexString(len1 & 0xff);
    }

    public static byte[] createMask() {
        byte[] mask = new byte[4];
        //noinspection ConstantConditions
        randomHolder.get().nextBytes(mask);
        return mask;
    }

    public static void applyMask(byte[] mask, byte[] data) {
        for (int i = 0; i < data.length; i++) {
            data[i] ^= mask[i % 4];
        }
    }

    public static byte[] toBytes(String aReason) {
        return aReason.getBytes(WebSocketUtil.UTF_8);
    }
}
