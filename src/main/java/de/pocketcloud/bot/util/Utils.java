package de.pocketcloud.bot.util;

import de.pocketcloud.bot.Bot;
import de.pocketcloud.bot.api.CloudAPI;
import de.pocketcloud.bot.config.data.HttpData;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import org.apache.commons.httpclient.HttpStatus;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.awt.*;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.Map;

public class Utils {

    public static MessageEmbed errorOccurred(int statusCode) {
        return new EmbedBuilder()
                .setTitle("Failed | Error occurred")
                .setDescription("Check if you have provided the correct 'auth_key', and please check if the http server is operational.")
                .setColor(Color.RED)
                .setFooter("Error: Error occurred | Status Code: " + statusCode + " (" + HttpStatus.getStatusText(statusCode) + ")")
                .build();
    }

    public static MessageEmbed timedOut() {
        return new EmbedBuilder()
                .setTitle("Failed | Timed out")
                .setDescription("Check if the cloud is online.")
                .setColor(Color.RED)
                .setFooter("Error: Timed out")
                .build();
    }

    public static ResponseData createRequest(String route, String requestMethod) {
        return createRequest(route, requestMethod, null);
    }

    public static ResponseData createRequest(String route, String requestMethod, Map<String, String> queries) {
        try {
            HttpRequest.BodyPublisher bodyPublisher = HttpRequest.BodyPublishers.noBody();
            if ((requestMethod.equals(CloudAPI.POST) || requestMethod.equals(CloudAPI.PUT)) && !queries.isEmpty()) bodyPublisher = HttpRequest.BodyPublishers.ofString(getQueriesString(queries));

            HttpData data = Bot.getInstance().getConfig().getHttpData();
            HttpClient client = HttpClient.newBuilder().build();
            HttpRequest request = HttpRequest.newBuilder()
                    .version(HttpClient.Version.HTTP_2)
                    .uri(URI.create("http://" + data.host() + ":" + data.port() + route + ((queries != null && !queries.isEmpty()) ? "?" + getQueriesString(queries) : "")))
                    .method(requestMethod, bodyPublisher)
                    .timeout(Duration.ofSeconds(2))
                    .header("Content-Type", "application/json")
                    .headers("auth-key", data.authKey())
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            return new ResponseData(response.statusCode(), (response.body().trim().isBlank() ? null : new JSONParser().parse(response.body())));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String getQueriesString(Map<String, String> queries) {
        StringBuilder result = new StringBuilder();

        for (Map.Entry<String, String> entry : queries.entrySet()) {
            result.append(URLEncoder.encode(entry.getKey(), StandardCharsets.UTF_8));
            result.append("=");
            result.append(URLEncoder.encode(entry.getValue(), StandardCharsets.UTF_8));
            result.append("&");
        }

        String resultString = result.toString();
        return resultString.length() > 0 ? resultString.substring(0, resultString.length() - 1) : resultString;
    }

    public static long calculateTime(long startTime) {
        return System.currentTimeMillis() - startTime;
    }

    public static long milliTime() {
        return System.currentTimeMillis();
    }
}
