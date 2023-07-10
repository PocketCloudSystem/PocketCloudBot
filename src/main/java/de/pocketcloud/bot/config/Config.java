package de.pocketcloud.bot.config;

import de.pocketcloud.bot.config.data.BotToken;
import de.pocketcloud.bot.config.data.HttpData;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;

public class Config {

    private BotToken botToken;
    private HttpData http;

    public Config() {
        try {
            String content = Files.readString(Paths.get("config.json"));
            JSONObject data = (JSONObject) new JSONParser().parse(content);
            botToken = new BotToken((String) data.get("bot_token"));
            http = new HttpData(
                    (String) ((JSONObject) data.get("http")).get("host"),
                    ((Number) ((JSONObject) data.get("http")).get("port")).intValue(),
                    (String) ((JSONObject) data.get("http")).get("auth_key")
            );
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
    }

    public BotToken getBotToken() {
        return botToken;
    }

    public HttpData getHttpData() {
        return http;
    }
}
