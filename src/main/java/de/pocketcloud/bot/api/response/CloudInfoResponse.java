package de.pocketcloud.bot.api.response;

import java.util.ArrayList;

public record CloudInfoResponse(
        String version,
        ArrayList<String> developer,
        ArrayList<String> templates,
        ArrayList<String> runningServers,
        ArrayList<String> players,
        ArrayList<String> loadedPlugins,
        ArrayList<String> enabledPlugins,
        ArrayList<String> disabledPlugins,
        String networkAddress,
        int statusCode
) {

    public static CloudInfoResponse error(int statusCode) {
        return new CloudInfoResponse(null, null, null, null, null, null, null, null, null, statusCode);
    }
}
