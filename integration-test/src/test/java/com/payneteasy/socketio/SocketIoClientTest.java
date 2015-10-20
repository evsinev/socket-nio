package com.payneteasy.socketio;

import com.payneteasy.websocket.UnauthorizedException;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URL;

/**
 *
 */
public class SocketIoClientTest {

    private static final Logger LOG = LoggerFactory.getLogger(SocketIoClientTest.class);

    @Test
    public void test() throws IOException, UnauthorizedException {
        SocketIoClient client = new SocketIoClient();
        ISocketIoListener listener = new ISocketIoListener() {

            @Override
            public void onEvent(String aEventName, SocketIoContext aContext, Object... args) {
                LOG.debug("Event is {}", aEventName);
                aContext.ack();
            }

            @Override
            public void onHeartbeat() {
            }

            @Override
            public void onFailure(Throwable aError) {
                LOG.error("Error is", aError);
            }
        };

        SocketIoSession session = client.connect(new URL("http://localhost:8080/socket.io/1/?payber_id=2&date=1413757319241&sign=5855544a6c3139484b41564d637551757a4a4541482b732f66544e63317064524b4b6177723676574d77376a7150536a636436393451496747623073664336314a4a7274585249757858672f476e6c515039507a2b524a6b3048704a3447362f52307a64796b3852494d4651444f4a2b4e65757236434757657241414445517471674a444479617263385268316736524f3076323338727147765a734d6270706959674669637a546a54696a5a4a624434375738773839675a4f35764c7563753262335444524e6558767a725a386f624f2f4962627a3463797547764c3548304b4a37367032334d73535072356b5947425856684a5657795452324c46727557573470776c35724a645a467a736a63597679304b726472387373544368737337763836596668386533384d6e487368484c6c687852516a714b6c69366a687073596e516c6479754a4b646654366336545542475a67413d3d&v=1.2")
        );
        session.startAndWait(listener);
    }
}
