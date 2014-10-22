package com.payneteasy.socketio;

/**
 *
 */
public class SocketIoConfiguration implements ISocketIoConfiguration {

    final long handshakeConnectionTimeout;
    final long handshakeReadTimeout;

    private SocketIoConfiguration(Builder aBuilder){
        handshakeConnectionTimeout = aBuilder.theHandshakeConnectionTimeout;
        handshakeReadTimeout = aBuilder.theHandshakeReadTimeout;
    }

    @Override
    public long getHandshakeConnectionTimeout() {
        return handshakeConnectionTimeout;
    }

    @Override
    public long getHandshakeReadTimeout() {
        return handshakeReadTimeout;
    }

    public static SocketIoConfiguration getDefault() {
        return new Builder()
                .handshakeConnectionTimeout(120 * 1000)
                .handshakeReadTimeout(120 * 1000)
                .build();
    }

    public static class Builder {

        public Builder handshakeConnectionTimeout(long aHandshakeConnectionTimeout) {
            theHandshakeConnectionTimeout = aHandshakeConnectionTimeout;
            return this;
        }

        public Builder handshakeReadTimeout(long aHandshakeReadTimeout) {
            theHandshakeReadTimeout = aHandshakeReadTimeout;
            return this;
        }

        public SocketIoConfiguration build() {
            return new SocketIoConfiguration(this);
        }

        private long theHandshakeConnectionTimeout;
        private long theHandshakeReadTimeout;

    }
}
