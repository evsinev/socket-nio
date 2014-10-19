package com.payneteasy.socketio;

import com.payneteasy.websocket.MutableWebSocketFrame;
import com.payneteasy.websocket.WebSocketUtil;

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

        return new SocketIoMessage.Builder()
                .type(valueOf(parseInt(fields[FIELD_TYPE])))
                .id(getField(fields, FIELD_ID))
                .endPoint(getField(fields, FIELD_ENDPOINT))
                .data(getField(fields, FIELD_DATA))
                .build();
    }

    private String getField(String[] aFields, int aPosition) {
        if(aPosition < aFields.length ) {
            String value = aFields[aPosition];
            return WebSocketUtil.hasText(value) ? value : null;
        } else {
            return null;
        }
    }
}
