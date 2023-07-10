package de.pocketcloud.bot.util;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public record ResponseData(int statusCode, Object response) {

    public JSONObject responseAsObject() {
        return (JSONObject) response;
    }

    public JSONArray responseAsArray() {
        return (JSONArray) response;
    }
}
