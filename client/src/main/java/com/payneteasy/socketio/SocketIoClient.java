package com.payneteasy.socketio;

import com.payneteasy.websocket.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;

/**
 *
 */
public class SocketIoClient {

    private static final Logger LOG = LoggerFactory.getLogger(SocketIoClient.class);

    private final ISocketIoConfiguration config;
    private final WebSocketClient webSocketClient;

    public SocketIoClient(ISocketIoConfiguration aSocketIoConfig, IWebSocketConfiguration aWebSocketConfig) {
        webSocketClient = new WebSocketClient(aWebSocketConfig);
        config = aSocketIoConfig;
    }

    public SocketIoClient() {
        webSocketClient = new WebSocketClient();
        config = SocketIoConfiguration.getDefault();
    }

    public SocketIoSession connect(URL aUrl) throws IOException, UnauthorizedException {
        String connectionParameters =  getSocketIoConnectionParameters(aUrl);

        SocketIoHandshakeResponse info = parseConnectionInfo(connectionParameters);
        LOG.debug("Handshake response: {}", info);
        WebSocketSession webSession = createWebSocketSession(info, aUrl);

        return new SocketIoSession(webSession);
    }

    private String getSocketIoConnectionParameters(URL aUrl) throws IOException, UnauthorizedException {
        HttpURLConnection connection = (HttpURLConnection) aUrl.openConnection();
        connection.setConnectTimeout((int) config.getHandshakeConnectionTimeout());
        connection.setReadTimeout((int) config.getHandshakeReadTimeout());

        LOG.debug("Connecting [ ct={}, rt={} ] to {}", config.getHandshakeConnectionTimeout(), config.getHandshakeReadTimeout(), aUrl);
        connection.connect();
        try {
            checkResponseCode(connection);

            LineNumberReader in = new LineNumberReader(new InputStreamReader(connection.getInputStream()));
            try {
                String line = in.readLine();
                if(line==null) {
                    throw new ProtocolException("Empty line");
                }
                return line;

            } finally {
                in.close();
            }
        } finally {
            connection.disconnect();
        }
    }

    private void checkResponseCode(HttpURLConnection connection) throws IOException, UnauthorizedException {
        if(connection.getResponseCode() != 200) {
            if(connection.getResponseCode() == 401) {
                throw new UnauthorizedException(connection.getResponseMessage());
            } else {
                throw new IllegalStateException(connection.getResponseCode()+" "+connection.getResponseMessage());
            }
        }
    }

    private WebSocketSession createWebSocketSession(SocketIoHandshakeResponse aInfo, URL aUrl) throws IOException {
        String baseUrl = createBaseUrl(aUrl);

        WebSocketHandshakeRequest handshake = new WebSocketHandshakeRequest.Builder()
                .url(baseUrl+"websocket/"+aInfo.sessionId)
                .build();

        return webSocketClient.connect(handshake);
    }

    private String createBaseUrl(URL aUrl) {
        return aUrl.getProtocol()
                + "://"
                + aUrl.getHost()
                + ":"
                + aUrl.getPort()
                + aUrl.getPath();
    }

    private SocketIoHandshakeResponse parseConnectionInfo(String aLine) {
        String[] tokens = aLine.split(":");
        return new SocketIoHandshakeResponse.Builder()
                .sessionId(tokens[0])
                .heartbeatTimeout(Long.parseLong(tokens[1])*1000)
                .closingTimeout(Long.parseLong(tokens[2])*1000)
                .protocols(tokens[3].split(","))
                .build();
    }
}
