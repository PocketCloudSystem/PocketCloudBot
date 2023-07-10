package de.pocketcloud.bot.api.response;

import java.util.ArrayList;

public record TemplateListResponse(
        ArrayList<String> templates,
        int statusCode
) {

    public static TemplateListResponse error(int statusCode) {
        return new TemplateListResponse(new ArrayList<>(), statusCode);
    }
}
