package com.payneteasy.websocket;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

/**
 *
 */
public class WebSocketHandshake {

    private final URL url;

    public URL url() {
        return url;
    }

    private WebSocketHandshake(Builder aBuilder){
        url = aBuilder.url;
    }

    public static class Builder {

        public Builder() {
            theHeaders = new HashMap<String, String>();
        }

        public Builder url(String aUrl) {
            urlString = aUrl;
            try {
                url = new URL(aUrl);
            } catch (MalformedURLException e) {
                throw new IllegalArgumentException("Malformed URL: "+aUrl, e);
            }
            return this;
        }

        public WebSocketHandshake build() {
            return new WebSocketHandshake(this);
        }

        private URL url;
        private String urlString;
        private Map<String, String> theHeaders;

    }
}
