package de.pocketcloud.bot.api.response;

public record PlayerKickResponse(
        String message,
        boolean error,
        int statusCode
) {

    public static PlayerKickResponse error(int statusCode) {
        return new PlayerKickResponse(null, true, statusCode);
    }
}
