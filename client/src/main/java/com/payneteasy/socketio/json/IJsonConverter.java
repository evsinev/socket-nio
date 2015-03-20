package com.payneteasy.socketio.json;

import com.google.gson.JsonElement;

/**
 *
 */
public interface IJsonConverter {

    String toJson(Object aObject);

    /**
     * Only for development.
     */
    String toPrettyJson(Object aObject);

    JsonElement parse(String aJsonText);
}
