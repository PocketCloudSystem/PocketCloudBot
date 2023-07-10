package de.pocketcloud.bot.api.response;

public record ServerCommandExecuteResponse(
        String message,
        boolean error,
        int statusCode
) {

    public static ServerCommandExecuteResponse error(int statusCode) {
        return new ServerCommandExecuteResponse(null, true, statusCode);
    }
}
