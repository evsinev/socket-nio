package com.payneteasy.websocket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.SSLSocketFactory;
import java.io.EOFException;
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

    private final WebSocketFrameDecoder frameDecoder = new WebSocketFrameDecoder();
    private final HttpResponseDecoder httDecoder = new HttpResponseDecoder();
    private final MutableWebSocketFrame frame = new MutableWebSocketFrame();
    private final OutputQueue queue = new OutputQueue();

    public void connect(WebSocketHandshake aRequest, IWebSocketListener aListener) throws IOException {
        URL url = aRequest.url();

        Socket socket = createSocket(url);
        socket.setSoTimeout(120000);
        LOG.debug("Connecting to {}...", url);

        socket.connect(new InetSocketAddress(url.getHost(), url.getPort()));

        // http handshake
        OutputStream out = socket.getOutputStream();
        out.write(createHandshake(aRequest));

        InputStream inputStream = socket.getInputStream();
        httDecoder.decode(inputStream);

        // data transfer
        WebSocketWriterThread writerThread = new WebSocketWriterThread(queue, out, aListener);
        writerThread.start();

        WebSocketContext context = new WebSocketContext(queue);

        try {
            while(!Thread.currentThread().isInterrupted()) {
                frameDecoder.decode(inputStream, frame);
                LOG.debug("W-IN: {}", frame);
                aListener.onMessage(frame, context);
            }
        } catch (EOFException e) {
            LOG.debug("Connection closed");
        }

    }

    private byte[] createHandshake(WebSocketHandshake aRequest) {
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
        LOG.debug("handshake = \n{}", handshake);
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
        if(aUrl.getProtocol().equals("https")) {
            SSLSocketFactory sslsocketfactory = (SSLSocketFactory) SSLSocketFactory.getDefault();
            return sslsocketfactory.createSocket();
        } else if (aUrl.getProtocol().equals("http")) {
            return new Socket();
        } else {
            throw new IllegalStateException("Unknown protocol "+aUrl.getProtocol());
        }
    }
}
