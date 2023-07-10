package de.pocketcloud.bot.api.response;

public record ServerStartResponse(
        String message,
        boolean error,
        int statusCode
) {

    public static ServerStartResponse error(int statusCode) {
        return new ServerStartResponse(null, true, statusCode);
    }
}
