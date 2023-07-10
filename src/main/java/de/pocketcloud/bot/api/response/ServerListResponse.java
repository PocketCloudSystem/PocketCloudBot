package de.pocketcloud.bot.api.response;

import java.util.ArrayList;

public record ServerListResponse(
        String error,
        ArrayList<String> servers,
        int statusCode
) {

    public static ServerListResponse error(int statusCode) {
        return new ServerListResponse(null, new ArrayList<>(), statusCode);
    }
}
