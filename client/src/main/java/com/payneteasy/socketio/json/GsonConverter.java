package com.payneteasy.socketio.json;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

/**
 *
 */
public class GsonConverter implements IJsonConverter {

    private static final JsonParser JSON_PARSER = new JsonParser();
    private static final Gson gson = new Gson();

    @Override
    public String toJson(Object aObject) {
        return gson.toJson(aObject);
    }

    /**
     * Only for development.
     */
    @Override
    public String toPrettyJson(Object aObject) {
        return PrettyGsonHolder.INSTANCE.toJson(aObject);
    }

    @Override
    public JsonElement parse(String aJsonText) {
        return JSON_PARSER.parse(aJsonText);
    }

    private static class PrettyGsonHolder {
        private static final Gson INSTANCE = new GsonBuilder().setPrettyPrinting().create();
    }

}
