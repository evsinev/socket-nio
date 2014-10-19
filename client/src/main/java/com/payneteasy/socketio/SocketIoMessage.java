package com.payneteasy.socketio;

import com.payneteasy.websocket.WebSocketUtil;

import static com.payneteasy.websocket.WebSocketUtil.hasText;

/**
 *
 */
public class SocketIoMessage {


    public Type type;
    public String id;
    public String endPoint;
    public String data;

    public enum Type {
          DISCONNECT    (0)
        , CONNECT       (1)
        , HEARTBEAT     (2)
        , MESSAGE       (3)
        , JSON_MESSAGE  (4)
        , EVENT         (5)
        , ACK           (6)
        , ERROR         (7)
        , NOOP          (8)
        , UNKNOWN       (-1)
        ;

        protected final int code;

        Type(int aCode) {
            code = aCode;
        }

        public static Type valueOf(int aCode) {
            switch (aCode) {
                case 0: return DISCONNECT;
                case 1: return CONNECT;
                case 2: return HEARTBEAT;
                case 3: return MESSAGE;
                case 4: return JSON_MESSAGE;
                case 5: return EVENT;
                case 6: return ACK;
                case 7: return ERROR;
                case 8: return NOOP;

                default:
                    return UNKNOWN;
            }
        }

    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append("{ ").append(type);
        if(hasText(id)) {
            sb.append(", id=").append(id);
        }

        if(hasText(endPoint)) {
            sb.append(", endPoint=").append(endPoint);
        }

        if(hasText(data)) {
            sb.append(", data=").append(data);
        }

        sb.append(" }");

        return sb.toString();
    }


}
