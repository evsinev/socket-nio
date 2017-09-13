package com.payneteasy.websocket;

/**
 *
 */
public class WebSocketConfiguration implements IWebSocketConfiguration {

    public final long    connectionTimeout;
    public final long    readTimeout;
    public final long    writerNextFramePoolTimeout;
    public final boolean insertFrameAgainOnWriteError;

    private WebSocketConfiguration(Builder aBuilder) {
        connectionTimeout = aBuilder.theConnectionTimeout;
        readTimeout = aBuilder.theReadTimeout;
        writerNextFramePoolTimeout = aBuilder.theWriterNextFramePoolTimeout;
        insertFrameAgainOnWriteError = aBuilder.theInsertFrameAgainOnWriteError;
    }

    public long getConnectionTimeout() {
        return connectionTimeout;
    }

    public long getReadTimeout() {
        return readTimeout;
    }

    public long getWriterNextFramePoolTimeout() {
        return writerNextFramePoolTimeout;
    }

    @Override
    public boolean insertFrameAgainOnWriteError() {
        return insertFrameAgainOnWriteError;
    }

    public static IWebSocketConfiguration getDefault() {
        return new Builder()
                .connectionTimeout(120 * 1000)
                .readTimeout(120 * 1000)
                .writerNextFramePoolTimeout(300 * 1000)
                .insertFrameAgainOnWriteError(true)
                .build();
    }

    public static class Builder {

        public Builder connectionTimeout(long aConnectionTimeout) {
            theConnectionTimeout = aConnectionTimeout;
            return this;
        }

        public Builder readTimeout(long aReadTimeout) {
            theReadTimeout = aReadTimeout;
            return this;
        }

        public Builder writerNextFramePoolTimeout(long aWriterNextFramePoolTimeout) {
            theWriterNextFramePoolTimeout = aWriterNextFramePoolTimeout;
            return this;
        }

        public Builder insertFrameAgainOnWriteError(boolean aInsertFrameAgainOnWriteError) {
            theInsertFrameAgainOnWriteError = aInsertFrameAgainOnWriteError;
            return this;
        }
        public WebSocketConfiguration build() {
            return new WebSocketConfiguration(this);
        }

        private long    theConnectionTimeout;
        private long    theReadTimeout;
        private long    theWriterNextFramePoolTimeout;
        private boolean theInsertFrameAgainOnWriteError;

    }

}
