package com.payneteasy.server;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.payneteasy.socketio.ISocketIoListener;
import com.payneteasy.socketio.SocketIoContext;
import junit.framework.Assert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ExampleClientListener implements ISocketIoListener {
    private static final Logger LOG = LoggerFactory.getLogger(ExampleClientListener.class);

    private final Gson gson = new Gson();

    @Override
    public void onEvent(String aEventName, SocketIoContext aContext, Object... args) {
        JsonElement eventJsonElement = (JsonElement) args[0];

        ExampleRequest request = gson.fromJson(eventJsonElement, ExampleRequest.class);

        LOG.debug("Event name is {}, args: {}, object: {}", aEventName, args, request);
        Assert.assertEquals("Hello", request.getName());

        aContext.sendEvent("ExampleResponse", new ExampleResponse("Hello2"));
    }

    @Override
    public void onHeartbeat() {
    }

    @Override
    public void onFailure(Throwable aError) {

    }

}
