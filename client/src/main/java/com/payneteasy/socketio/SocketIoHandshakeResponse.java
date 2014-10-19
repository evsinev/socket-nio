package com.payneteasy.socketio;

import java.util.Arrays;

/**
 *
 */
public class SocketIoHandshakeResponse {

    public final String sessionId;
    public final long heartbeatTimeout;
    public final long closingTimeout;
    public final String[] protocols;

    private SocketIoHandshakeResponse(Builder aBuilder){
        sessionId = aBuilder.theSessionId;
        heartbeatTimeout = aBuilder.theHeartbeatTimeout;
        closingTimeout = aBuilder.theClosingTimeout;
        protocols = aBuilder.theProtocols;
    }


    @Override
    public String toString() {
        return "SocketIoHandshakeResponse{" +
                "sessionId='" + sessionId + '\'' +
                ", heartbeatTimeout=" + heartbeatTimeout +
                ", closingTimeout=" + closingTimeout +
                ", protocols=" + Arrays.toString(protocols) +
                '}';
    }

    public static class Builder {

        public Builder sessionId(String aSessionId) {
            theSessionId = aSessionId;
            return this;
        }

        public Builder heartbeatTimeout(long aHeartbeatTimeout) {
            theHeartbeatTimeout = aHeartbeatTimeout;
            return this;
        }

        public Builder closingTimeout(long aClosingTimeout) {
            theClosingTimeout = aClosingTimeout;
            return this;
        }

        public Builder protocols(String[] aProtocols) {
            theProtocols = aProtocols;
            return this;
        }

        public SocketIoHandshakeResponse build() {
            return new SocketIoHandshakeResponse(this);
        }

        private String theSessionId;
        private long theHeartbeatTimeout;
        private long theClosingTimeout;
        private String[] theProtocols;

    }
}
