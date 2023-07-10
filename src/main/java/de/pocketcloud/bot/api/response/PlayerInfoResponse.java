package de.pocketcloud.bot.api.response;

public record PlayerInfoResponse(
        String error,
        String name,
        String host,
        String xboxUserId,
        String uniqueId,
        String currentServer,
        String currentProxy,
        int statusCode
) {

    public static PlayerInfoResponse error(int statusCode) {
        return new PlayerInfoResponse(null, null, null, null, null, null, null, statusCode);
    }
}
