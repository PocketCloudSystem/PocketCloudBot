package de.pocketcloud.bot.api;

import de.pocketcloud.bot.Bot;
import de.pocketcloud.bot.api.response.*;
import de.pocketcloud.bot.command.impl.server.ServerInfoCommand;
import de.pocketcloud.bot.util.ResponseData;
import de.pocketcloud.bot.util.Utils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public class CloudAPI {

    public static final String GET = "GET";
    public static final String POST = "POST";
    public static final String PUT = "PUT";
    public static final String PATCH = "PATCH";
    public static final String DELETE = "DELETE";

    public static CompletableFuture<CloudInfoResponse> getCloudInformation() {
        return CompletableFuture.supplyAsync(() -> {
            ResponseData responseData = Utils.createRequest("/cloud/info/", GET);
            if (responseData == null) return CloudInfoResponse.error(-1);
            if (responseData.statusCode() != 200) return CloudInfoResponse.error(responseData.statusCode());
            return new CloudInfoResponse(
                    (String) responseData.responseAsObject().get("version"),
                    (ArrayList<String>) responseData.responseAsObject().get("developer"),
                    (ArrayList<String>) responseData.responseAsObject().get("templates"),
                    (ArrayList<String>) responseData.responseAsObject().get("runningServers"),
                    (ArrayList<String>) responseData.responseAsObject().get("players"),
                    (ArrayList<String>) responseData.responseAsObject().get("loadedPlugins"),
                    (ArrayList<String>) responseData.responseAsObject().get("enabledPlugins"),
                    (ArrayList<String>) responseData.responseAsObject().get("disabledPlugins"),
                    (String) responseData.responseAsObject().get("network_address"),
                    responseData.statusCode()
            );
        }, Bot.getInstance().getThreadPool());
    }

    public static CompletableFuture<PlayerKickResponse> kickPlayer(String player, String reason) {
        return CompletableFuture.supplyAsync(() -> {
            ResponseData responseData = Utils.createRequest("/player/kick/", POST, Map.of("identifier", player, "reason", reason));
            if (responseData == null) return PlayerKickResponse.error(-1);
            if (responseData.statusCode() != 200) return PlayerKickResponse.error(responseData.statusCode());
            return new PlayerKickResponse(
                    (String) responseData.responseAsObject().getOrDefault("success", responseData.responseAsObject().get("error")),
                    responseData.responseAsObject().containsKey("error"),
                    responseData.statusCode()
            );
        });
    }

    public static CompletableFuture<PlayerTextResponse> textPlayer(String player, String type, String text) {
        return CompletableFuture.supplyAsync(() -> {
            ResponseData responseData = Utils.createRequest("/player/text", POST, Map.of("identifier", player, "text_type", type, "text", text));
            if (responseData == null) return PlayerTextResponse.error(-1);
            if (responseData.statusCode() != 200) return PlayerTextResponse.error(responseData.statusCode());
            return new PlayerTextResponse(
                    (String) responseData.responseAsObject().getOrDefault("success", responseData.responseAsObject().get("error")),
                    responseData.responseAsObject().containsKey("error"),
                    responseData.statusCode()
            );
        });
    }

    public static CompletableFuture<PlayerInfoResponse> getPlayerInformation(String player) {
        return CompletableFuture.supplyAsync(() -> {
            ResponseData responseData = Utils.createRequest("/player/get/", GET, Map.of("identifier", player));
            if (responseData == null) return PlayerInfoResponse.error(-1);
            if (responseData.statusCode() != 200) return PlayerInfoResponse.error(responseData.statusCode());
            return new PlayerInfoResponse(
                    (String) responseData.responseAsObject().getOrDefault("error", ""),
                    (String) responseData.responseAsObject().getOrDefault("name", ""),
                    (String) responseData.responseAsObject().getOrDefault("host", ""),
                    (String) responseData.responseAsObject().getOrDefault("xboxUserId", ""),
                    (String) responseData.responseAsObject().getOrDefault("uniqueId", ""),
                    (String) responseData.responseAsObject().getOrDefault("currentServer", ""),
                    (String) responseData.responseAsObject().getOrDefault("currentProxy", ""),
                    responseData.statusCode()
            );
        });
    }

    public static CompletableFuture<PluginInfoResponse> getPluginInformation(String plugin) {
        return CompletableFuture.supplyAsync(() -> {
            ResponseData responseData = Utils.createRequest("/plugin/get/", GET, Map.of("plugin", plugin));
            if (responseData == null) return PluginInfoResponse.error(-1);
            if (responseData.statusCode() != 200) return PluginInfoResponse.error(responseData.statusCode());
            return new PluginInfoResponse(
                    (String) responseData.responseAsObject().getOrDefault("error", ""),
                    (String) responseData.responseAsObject().getOrDefault("name", ""),
                    (String) responseData.responseAsObject().getOrDefault("main", ""),
                    (String) responseData.responseAsObject().getOrDefault("version", ""),
                    (ArrayList<String>) responseData.responseAsObject().getOrDefault("authors", new ArrayList<>()),
                    (String) responseData.responseAsObject().getOrDefault("description", ""),
                    responseData.responseAsObject().getOrDefault("enabled", "false").toString().equalsIgnoreCase("true"),
                    responseData.statusCode()
            );
        });
    }

    public static CompletableFuture<ServerStartResponse> startServer(String template, int count) {
        return CompletableFuture.supplyAsync(() -> {
            ResponseData responseData = Utils.createRequest("/server/start/", POST, Map.of("template", template, "count", String.valueOf(count)));
            if (responseData == null) return ServerStartResponse.error(-1);
            if (responseData.statusCode() != 200) return ServerStartResponse.error(responseData.statusCode());
            return new ServerStartResponse(
                    (String) responseData.responseAsObject().getOrDefault("success", responseData.responseAsObject().get("error")),
                    responseData.responseAsObject().containsKey("error"),
                    responseData.statusCode()
            );
        });
    }

    public static CompletableFuture<ServerStopResponse> stopServer(String server) {
        return CompletableFuture.supplyAsync(() -> {
            ResponseData responseData = Utils.createRequest("/server/stop/", POST, Map.of("identifier", server));
            if (responseData == null) return ServerStopResponse.error(-1);
            if (responseData.statusCode() != 200) return ServerStopResponse.error(responseData.statusCode());
            return new ServerStopResponse(
                    (String) responseData.responseAsObject().getOrDefault("success", responseData.responseAsObject().get("error")),
                    responseData.responseAsObject().containsKey("error"),
                    responseData.statusCode()
            );
        });
    }

    public static CompletableFuture<ServerSaveResponse> saveServer(String server) {
        return CompletableFuture.supplyAsync(() -> {
            ResponseData responseData = Utils.createRequest("/server/save/", POST, Map.of("server", server));
            if (responseData == null) return ServerSaveResponse.error(-1);
            if (responseData.statusCode() != 200) return ServerSaveResponse.error(responseData.statusCode());
            return new ServerSaveResponse(
                    (String) responseData.responseAsObject().getOrDefault("success", responseData.responseAsObject().get("error")),
                    responseData.responseAsObject().containsKey("error"),
                    responseData.statusCode()
            );
        });
    }
    
    public static CompletableFuture<ServerCommandExecuteResponse> executeCommand(String server, String command) {
        return CompletableFuture.supplyAsync(() -> {
            ResponseData responseData = Utils.createRequest("/server/execute/", POST, Map.of("server", server, "command", command));
            if (responseData == null) return ServerCommandExecuteResponse.error(-1);
            if (responseData.statusCode() != 200) return ServerCommandExecuteResponse.error(responseData.statusCode());
            return new ServerCommandExecuteResponse(
                    (String) responseData.responseAsObject().getOrDefault("success", responseData.responseAsObject().get("error")),
                    responseData.responseAsObject().containsKey("error"),
                    responseData.statusCode()
            );
        });
    }

    public static CompletableFuture<TemplateInfoResponse> getTemplateInformation(String template) {
        return CompletableFuture.supplyAsync(() -> {
            ResponseData responseData = Utils.createRequest("/template/get/", GET, Map.of("name", template));
            if (responseData == null) return TemplateInfoResponse.error(-1);
            if (responseData.statusCode() != 200) return TemplateInfoResponse.error(responseData.statusCode());
            return new TemplateInfoResponse(
                    (String) responseData.responseAsObject().getOrDefault("error", ""),
                    (String) responseData.responseAsObject().getOrDefault("name", ""),
                    responseData.responseAsObject().getOrDefault("lobby", "false").toString().equalsIgnoreCase("true"),
                    responseData.responseAsObject().getOrDefault("maintenance", "false").toString().equalsIgnoreCase("true"),
                    responseData.responseAsObject().getOrDefault("static", "false").toString().equalsIgnoreCase("true"),
                    Integer.parseInt(responseData.responseAsObject().getOrDefault("maxPlayerCount", "0").toString()),
                    Integer.parseInt(responseData.responseAsObject().getOrDefault("minServerCount", "0").toString()),
                    Integer.parseInt(responseData.responseAsObject().getOrDefault("maxServerCount", "0").toString()),
                    responseData.responseAsObject().getOrDefault("startNewWhenFull", "false").toString().equalsIgnoreCase("true"),
                    responseData.responseAsObject().getOrDefault("autoStart", "false").toString().equalsIgnoreCase("true"),
                    (String) responseData.responseAsObject().getOrDefault("templateType", ""),
                    responseData.statusCode()
            );
        });
    }

    public static CompletableFuture<ServerInfoResponse> getServerInformation(String server) {
        return CompletableFuture.supplyAsync(() -> {
            ResponseData responseData = Utils.createRequest("/server/get/", GET, Map.of("identifier", server));
            if (responseData == null) return ServerInfoResponse.error(-1);
            if (responseData.statusCode() != 200) return ServerInfoResponse.error(responseData.statusCode());
            ArrayList<HashMap<String, Object>> servers = new ArrayList<>();

            if (responseData.response() instanceof JSONArray) servers = (ArrayList<HashMap<String, Object>>) responseData.responseAsArray();
            else servers.add(responseData.responseAsObject());

            String error = "";
            if (responseData.response() instanceof JSONObject) error = (String) responseData.responseAsObject().getOrDefault("error", "");
            return new ServerInfoResponse(
                    error,
                    servers,
                    responseData.statusCode()
            );
        });
    }

    public static CompletableFuture<PluginListResponse> listPlugins() {
        return CompletableFuture.supplyAsync(() -> {
            ResponseData responseData = Utils.createRequest("/plugin/list/", GET);
            if (responseData == null) return PluginListResponse.error(-1);
            if (responseData.statusCode() != 200) return PluginListResponse.error(responseData.statusCode());
            return new PluginListResponse(
                    (ArrayList<String>) responseData.responseAsObject().getOrDefault("loadedPlugins", new ArrayList<>()),
                    (ArrayList<String>) responseData.responseAsObject().getOrDefault("enabledPlugins", new ArrayList<>()),
                    (ArrayList<String>) responseData.responseAsObject().getOrDefault("disabledPlugins", new ArrayList<>()),
                    responseData.statusCode()
            );
        });
    }

    public static CompletableFuture<PlayerListResponse> listPlayers() {
        return CompletableFuture.supplyAsync(() -> {
            ResponseData responseData = Utils.createRequest("/player/list/", GET);
            if (responseData == null) return PlayerListResponse.error(-1);
            if (responseData.statusCode() != 200) return PlayerListResponse.error(responseData.statusCode());
            return new PlayerListResponse(
                    responseData.responseAsArray(),
                    responseData.statusCode()
            );
        });
    }

    public static CompletableFuture<TemplateListResponse> listTemplates() {
        return CompletableFuture.supplyAsync(() -> {
            ResponseData responseData = Utils.createRequest("/template/list/", GET);
            if (responseData == null) return TemplateListResponse.error(-1);
            if (responseData.statusCode() != 200) return TemplateListResponse.error(responseData.statusCode());
            return new TemplateListResponse(
                    responseData.responseAsArray(),
                    responseData.statusCode()
            );
        });
    }

    public static CompletableFuture<ServerListResponse> listServers() {
        return listServers(null);
    }

    public static CompletableFuture<ServerListResponse> listServers(String template) {
        return CompletableFuture.supplyAsync(() -> {
            Map<String, String> queries = new java.util.HashMap<>();
            if (template != null) queries.put("template", template);
            ResponseData responseData = Utils.createRequest("/server/list/", GET, queries);
            if (responseData == null) return ServerListResponse.error(-1);
            if (responseData.statusCode() != 200) return ServerListResponse.error(responseData.statusCode());
            String error = "";
            if (responseData.response() instanceof JSONObject) error = (String) responseData.responseAsObject().getOrDefault("error", "");
            return new ServerListResponse(
                    error,
                    (error.isBlank() ? responseData.responseAsArray() : new ArrayList<>()),
                    responseData.statusCode()
            );
        });
    }
}
