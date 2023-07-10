package de.pocketcloud.bot.api.response;

import java.util.ArrayList;

public record PlayerListResponse(
        ArrayList<String> players,
        int statusCode
) {

    public static PlayerListResponse error(int statusCode) {
        return new PlayerListResponse(new ArrayList<>(), statusCode);
    }
}
