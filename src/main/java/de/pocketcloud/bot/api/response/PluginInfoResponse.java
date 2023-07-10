package de.pocketcloud.bot.api.response;

import java.util.ArrayList;

public record PluginInfoResponse(
        String error,
        String name,
        String main,
        String version,
        ArrayList<String> authors,
        String description,
        boolean enabled,
        int statusCode
) {

    public static PluginInfoResponse error(int statusCode) {
        return new PluginInfoResponse(null, null, null, null, null, null, false, statusCode);
    }
}
