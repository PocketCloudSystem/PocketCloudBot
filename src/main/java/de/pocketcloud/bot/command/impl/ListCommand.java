package de.pocketcloud.bot.command.impl;

import de.pocketcloud.bot.api.CloudAPI;
import de.pocketcloud.bot.command.Command;
import de.pocketcloud.bot.util.Utils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.SlashCommandInteraction;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.dv8tion.jda.api.utils.messages.MessageEditData;

import java.awt.*;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class ListCommand extends Command {

    public ListCommand() {
        super(Commands.slash("list", "List every server, template, player or plugin")
                .addOptions(
                        new OptionData(OptionType.STRING, "type", "Type for the list", true)
                                .addChoice("templates", "TEMPLATES")
                                .addChoice("servers", "SERVERS")
                                .addChoice("players", "PLAYERS")
                                .addChoice("plugins", "PLUGINS"),
                        new OptionData(OptionType.STRING, "template", "Template for SERVERS type")
                )
        );
    }

    @Override
    public void execute(SlashCommandInteractionEvent event, SlashCommandInteraction interaction, List<OptionMapping> options, long startTime) {
        String type = event.getOption("type", OptionMapping::getAsString);
        String template = event.getOption("template", OptionMapping::getAsString);

        switch (Objects.requireNonNull(type).toUpperCase(Locale.ROOT)) {
            case "TEMPLATES" -> {
                CloudAPI.listTemplates().thenAccept(response -> {
                    if (response.statusCode() == -1) {
                        event.getHook().editOriginal(MessageEditData.fromEmbeds(Utils.timedOut())).queue();
                        return;
                    }

                    if (response.statusCode() != 200) {
                        event.getHook().editOriginal(MessageEditData.fromEmbeds(Utils.errorOccurred(response.statusCode()))).queue();
                        return;
                    }

                    EmbedBuilder builder = new EmbedBuilder();
                    builder.setColor(Color.GREEN);
                    builder.setTitle("List of all templates");
                    builder.addField("Templates", (response.templates().isEmpty() ? "> No templates" : "> " + String.join("\n> ", response.templates())), true);
                    builder.setFooter("Response time (RestAPI): " + Utils.calculateTime(startTime) + "ms");
                    event.getHook().editOriginal(MessageEditData.fromEmbeds(builder.build())).queue();
                });
            }
            case "SERVERS" -> {
                CloudAPI.listServers(template).thenAccept(response -> {
                    if (response.statusCode() == -1) {
                        event.getHook().editOriginal(MessageEditData.fromEmbeds(Utils.timedOut())).queue();
                        return;
                    }

                    if (response.statusCode() != 200) {
                        event.getHook().editOriginal(MessageEditData.fromEmbeds(Utils.errorOccurred(response.statusCode()))).queue();
                        return;
                    }

                    EmbedBuilder builder = new EmbedBuilder();
                    if (!response.error().isBlank()) {
                        builder.setTitle("Failed | List servers");
                        builder.setColor(Color.RED);
                        builder.setDescription(response.error());
                    } else {
                        builder.setTitle("List of all servers");
                        builder.addField("Servers", (response.servers().isEmpty() ? "> No servers" : "> " + String.join("\n> ", response.servers())), true);
                        builder.setColor(Color.GREEN);
                    }

                    builder.setFooter("Response time (RestAPI): " + Utils.calculateTime(startTime) + "ms");
                    event.getHook().editOriginal(MessageEditData.fromEmbeds(builder.build())).queue();
                });
            }
            case "PLAYERS" -> {
                CloudAPI.listPlayers().thenAccept(response -> {
                    if (response.statusCode() == -1) {
                        event.getHook().editOriginal(MessageEditData.fromEmbeds(Utils.timedOut())).queue();
                        return;
                    }

                    if (response.statusCode() != 200) {
                        event.getHook().editOriginal(MessageEditData.fromEmbeds(Utils.errorOccurred(response.statusCode()))).queue();
                        return;
                    }

                    EmbedBuilder builder = new EmbedBuilder();
                    builder.setColor(Color.GREEN);
                    builder.setTitle("List of all players");
                    builder.addField("Players", (response.players().isEmpty() ? "> No players" : "> " + String.join("\n> ", response.players())), true);
                    builder.setFooter("Response time (RestAPI): " + Utils.calculateTime(startTime) + "ms");
                    event.getHook().editOriginal(MessageEditData.fromEmbeds(builder.build())).queue();
                });
            }
            case "PLUGINS" -> {
                CloudAPI.listPlugins().thenAccept(response -> {
                    if (response.statusCode() == -1) {
                        event.getHook().editOriginal(MessageEditData.fromEmbeds(Utils.timedOut())).queue();
                        return;
                    }

                    if (response.statusCode() != 200) {
                        event.getHook().editOriginal(MessageEditData.fromEmbeds(Utils.errorOccurred(response.statusCode()))).queue();
                        return;
                    }

                    EmbedBuilder builder = new EmbedBuilder();
                    builder.setColor(Color.GREEN);
                    builder.setTitle("List of all plugins");
                    builder.addField("Loaded Plugins", (response.loadedPlugins().isEmpty() ? "> No plugins" : "> " + String.join("\n> ", response.loadedPlugins())), true);
                    builder.addField("Enabled Plugins", (response.enabledPlugins().isEmpty() ? "> No enabled plugins" : "> " + String.join("\n> ", response.enabledPlugins())), true);
                    builder.addField("Disabled Plugins", (response.disabledPlugins().isEmpty() ? "> No disabled plugins" : "> " + String.join("\n> ", response.disabledPlugins())), true);
                    builder.setFooter("Response time (RestAPI): " + Utils.calculateTime(startTime) + "ms");
                    event.getHook().editOriginal(MessageEditData.fromEmbeds(builder.build())).queue();
                });
            }
        }
    }
}
