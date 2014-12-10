package com.payneteasy.server;

import com.corundumstudio.socketio.Configuration;
import com.corundumstudio.socketio.SocketConfig;
import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.handler.SuccessAuthorizationListener;
import com.payneteasy.socketio.ISocketIoListener;
import com.payneteasy.socketio.SocketIoClient;
import com.payneteasy.socketio.SocketIoConfiguration;
import com.payneteasy.socketio.SocketIoSession;
import com.payneteasy.websocket.UnauthorizedException;
import com.payneteasy.websocket.WebSocketConfiguration;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URL;

public class WebSocketServerTest {

    private static final Logger LOG = LoggerFactory.getLogger(WebSocketServerTest.class);

    public SocketIOServer startServer() {
        Configuration config = new Configuration();
        config.setHostname("localhost");
        config.setAuthorizationListener(new SuccessAuthorizationListener());
        config.setPort(9091);

        SocketConfig socketConfig = new SocketConfig();
        socketConfig.setReuseAddress(true);
        socketConfig.setTcpNoDelay(true);
        config.setSocketConfig(socketConfig);

        config.setBossThreads(1);
        config.setWorkerThreads(1);

        SocketIOServer server = new SocketIOServer(config);
        server.addListeners(new ExampleServerListener());

        server.start();

        return server;
    }


    @Test
    public void test() throws IOException, UnauthorizedException {

        final SocketIOServer server = startServer();

        try {
            ISocketIoListener listener = new ExampleClientListener();

            SocketIoClient client = new SocketIoClient(SocketIoConfiguration.getDefault(), WebSocketConfiguration.getDefault());
            String url = "http://localhost:9091/socket.io/1/";
            SocketIoSession session = client.connect(new URL(url));
            stopThreadAfter5Seconds(server, session);

            LOG.debug("Starting session");
            session.startAndWait(listener);

            LOG.debug("Closing session");
            session.close();
        } finally {
            server.stop();
        }

    }

    private void stopThreadAfter5Seconds(final SocketIOServer server, final SocketIoSession aClient) {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                server.stop();
                try {
                    aClient.close();
                } catch (IOException e) {
                    LOG.error("Can't close error", e);
                }
            }
        });
        thread.setDaemon(true);
        thread.start();
    }

}
