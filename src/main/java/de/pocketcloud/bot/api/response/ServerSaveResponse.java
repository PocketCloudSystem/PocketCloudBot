package de.pocketcloud.bot.api.response;

public record ServerSaveResponse(
        String message,
        boolean error,
        int statusCode
) {

    public static ServerSaveResponse error(int statusCode) {
        return new ServerSaveResponse(null, true, statusCode);
    }
}