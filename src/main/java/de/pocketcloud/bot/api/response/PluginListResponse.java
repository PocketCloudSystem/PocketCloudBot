package de.pocketcloud.bot.api.response;

import java.util.ArrayList;

public record PluginListResponse(
        ArrayList<String> loadedPlugins,
        ArrayList<String> enabledPlugins,
        ArrayList<String> disabledPlugins,
        int statusCode
) {

    public static PluginListResponse error(int statusCode) {
        return new PluginListResponse(new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), statusCode);
    }
}