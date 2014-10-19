package com.payneteasy.socketio.json;

import com.google.gson.JsonElement;

/**
 *
 */
public interface IJsonConverter {

    String toJson(Object aObject);

    JsonElement parse(String aJsonText);
}
