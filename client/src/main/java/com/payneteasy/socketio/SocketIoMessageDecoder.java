package com.payneteasy.socketio;

import com.payneteasy.websocket.MutableWebSocketFrame;

import static com.payneteasy.socketio.SocketIoMessage.Type.valueOf;
import static java.lang.Integer.parseInt;

/**
 *
 */
public class SocketIoMessageDecoder {

    /** Index of the type field in a message */
    public static final int FIELD_TYPE = 0;

    /** Index of the id field in a message */
    public static final int FIELD_ID = 1;

    /** Index of the end point field in a message */
    public static final int FIELD_ENDPOINT = 2;

    /** Index of the data field in a message */
    public static final int FIELD_DATA = 3;


    public SocketIoMessage decode(MutableWebSocketFrame aFrame) {

        String text = aFrame.getTextData();

        String[] fields = text.split(":", 4);

        SocketIoMessage message = new SocketIoMessage();
        message.type = valueOf(parseInt(fields[FIELD_TYPE]));

        message.id       = getField(fields, FIELD_ID);
        message.endPoint = getField(fields, FIELD_ENDPOINT);
        message.data     = getField(fields, FIELD_DATA);

        return message;
    }

    private String getField(String[] aFields, int aPosition) {
        return aPosition < aFields.length ? aFields[aPosition] : null;
    }
}
