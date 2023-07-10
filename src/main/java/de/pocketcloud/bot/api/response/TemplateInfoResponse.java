package de.pocketcloud.bot.api.response;

public record TemplateInfoResponse(
        String error,
        String name,
        boolean lobby,
        boolean maintenance,
        boolean staticServers,
        int maxPlayerCount,
        int minServerCount,
        int maxServerCount,
        boolean startNewWhenFull,
        boolean autoStart,
        String templateType,
        int statusCode
) {

    public static TemplateInfoResponse error(int statusCode) {
        return new TemplateInfoResponse(null, null, false, false, false, -1, -1, -1, false, false, null, statusCode);
    }
}
