package de.pocketcloud.bot.api.response;

public record PlayerTextResponse(
        String message,
        boolean error,
        int statusCode
) {

    public static PlayerTextResponse error(int statusCode) {
        return new PlayerTextResponse(null, true, statusCode);
    }
}
