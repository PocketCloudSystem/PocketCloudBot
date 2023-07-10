package de.pocketcloud.bot.api.response;

import java.util.ArrayList;
import java.util.HashMap;

public record ServerInfoResponse(
        String error,
        ArrayList<HashMap<String, Object>> servers,
        int statusCode
) {

    public static ServerInfoResponse error(int statusCode) {
        return new ServerInfoResponse(null, new ArrayList<>(), statusCode);
    }
}