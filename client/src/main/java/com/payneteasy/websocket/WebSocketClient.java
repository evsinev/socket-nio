package com.payneteasy.websocket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.SSLSocketFactory;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.URL;

/**
 *
 */
public class WebSocketClient {

    private static final Logger LOG = LoggerFactory.getLogger(WebSocketClient.class);
    public static final String CR_LF = "\n\r";

    private final HttpResponseDecoder httDecoder = new HttpResponseDecoder();
    private final IWebSocketConfiguration config;

    public WebSocketClient() {
        config = WebSocketConfiguration.getDefault();
    }

    public WebSocketClient(IWebSocketConfiguration aConfiguration) {
         config = aConfiguration;
    }

    public WebSocketSession connect(WebSocketHandshakeRequest aRequest) throws IOException {
        URL url = aRequest.url();

        Socket socket = createSocket(url);
        socket.setSoTimeout((int) config.getReadTimeout());
        try {
            LOG.debug("Connecting [ ct={}, rt={} ] to {}  ...", config.getConnectionTimeout(), config.getReadTimeout(), url);

            socket.connect(new InetSocketAddress(url.getHost(), url.getPort()), (int) config.getConnectionTimeout());

            /*if (true) {
                throw new IOException("Manually break socket for testing.");
            }*/

            // http handshake
            OutputStream out = socket.getOutputStream();
            out.write(createHandshake(aRequest));

            InputStream inputStream = socket.getInputStream();
            httDecoder.decode(inputStream);

            return new WebSocketSession(socket, out, inputStream, config);
        } catch (IOException e) {
            closeSocketSafe(socket);
            throw e;
        } catch (RuntimeException e) {
            closeSocketSafe(socket);
            throw e;
        }
    }

    private void closeSocketSafe(Socket socket) {
        try {
            socket.close();
        } catch (Exception e) {
            LOG.error("Unable to close socket.", e);
        }
    }


    private byte[] createHandshake(WebSocketHandshakeRequest aRequest) {
        StringBuilder sb = new StringBuilder();

        // request line
        sb.append("GET ");
        appendRequestPath(sb, aRequest.url());
        sb.append(" HTTP/1.1");
        sb.append(CR_LF);

        // host
        sb.append("Host: ").append(aRequest.url().getHost()).append(CR_LF);

        //
        sb.append("Connection: Upgrade").append(CR_LF);
        sb.append("Upgrade: websocket").append(CR_LF);
        sb.append("Sec-WebSocket-Key: 1GhlIHNhbXBsZSBub25jZQ==").append(CR_LF);
        sb.append("Sec-WebSocket-Version: 13").append(CR_LF);

        sb.append(CR_LF);

        String handshake = sb.toString();
        return handshake.getBytes();
    }

    private void appendRequestPath(StringBuilder aSb, URL aUrl) {
        aSb.append(aUrl.getPath());
        String query = aUrl.getQuery();
        if(query !=null && query.length()>0) {
            aSb.append('?');
            aSb.append(query);
        }
    }

    private Socket createSocket(URL aUrl) throws IOException {
        // https
        if(aUrl.getProtocol().equals("https")) {
            SSLSocketFactory sslsocketfactory = (SSLSocketFactory) SSLSocketFactory.getDefault();
            return sslsocketfactory.createSocket();

        // http
        } else if (aUrl.getProtocol().equals("http")) {
            return new Socket();

        // unknown
        } else {
            throw new IllegalStateException("Unknown protocol "+aUrl.getProtocol());
        }
    }
}
