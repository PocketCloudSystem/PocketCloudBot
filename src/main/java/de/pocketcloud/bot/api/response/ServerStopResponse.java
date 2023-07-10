package de.pocketcloud.bot.api.response;

public record ServerStopResponse(
        String message,
        boolean error,
        int statusCode
) {

    public static ServerStopResponse error(int statusCode) {
        return new ServerStopResponse(null, true, statusCode);
    }
}
