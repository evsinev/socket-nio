package com.payneteasy.websocket;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

/**
 *
 */
public class WebSocketHandshakeRequest {

    private final URL url;

    public URL url() {
        return url;
    }

    private WebSocketHandshakeRequest(Builder aBuilder){
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

        public WebSocketHandshakeRequest build() {
            return new WebSocketHandshakeRequest(this);
        }

        private URL url;
        private String urlString;
        private Map<String, String> theHeaders;

    }
}
