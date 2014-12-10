package com.payneteasy.server;

import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.annotation.OnConnect;
import com.corundumstudio.socketio.annotation.OnEvent;
import junit.framework.Assert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ExampleServerListener {

    private static final Logger LOG = LoggerFactory.getLogger(ExampleServerListener.class);

    @OnConnect
    public void onConnectHandler(SocketIOClient client) {
        LOG.debug("Got connection");
        ExampleRequest payload = new ExampleRequest();
        payload.setName("Hello");
        client.sendEvent("TestEvent", payload);
    }


    @OnEvent("ExampleResponse")
    public void onExampleResponse(SocketIOClient client, ExampleResponse aResponse) {
        LOG.debug("Response is {}", aResponse);
        Assert.assertEquals("Hello2", aResponse.getName());
        client.disconnect();
    }

}
