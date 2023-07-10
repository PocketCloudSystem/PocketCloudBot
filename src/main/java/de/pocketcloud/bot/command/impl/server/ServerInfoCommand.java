package de.pocketcloud.bot.command.impl.server;

import de.pocketcloud.bot.api.CloudAPI;
import de.pocketcloud.bot.command.Command;
import de.pocketcloud.bot.util.Utils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.SlashCommandInteraction;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.utils.messages.MessageEditData;

import java.awt.*;
import java.util.HashMap;
import java.util.List;

public class ServerInfoCommand extends Command {

    public ServerInfoCommand() {
        super(Commands.slash("server-info", "Get information about a server or servers with from a specific template")
                .addOption(OptionType.STRING, "server", "Name of the server or template", true)
        );
    }

    @Override
    public void execute(SlashCommandInteractionEvent event, SlashCommandInteraction interaction, List<OptionMapping> options, long startTime) {
        String server = event.getOption("server", OptionMapping::getAsString);

        CloudAPI.getServerInformation(server).thenAccept(response -> {
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
                builder.setTitle("Failed | Get server");
                builder.setColor(Color.RED);
                builder.setDescription(response.error());
            } else {
                builder.setTitle("Success | Get server");
                builder.setColor(Color.GREEN);
                if (response.servers().size() == 1) {
                    builder.addField("Name", "> " + response.servers().get(0).get("name"), true);
                    builder.addField("Id", "> " + response.servers().get(0).get("id"), true);
                    builder.addField("Template", "> " + response.servers().get(0).get("template"), true);
                    builder.addField("MaxPlayers", "> " + response.servers().get(0).get("maxPlayers"), true);
                    builder.addField("ProcessId", "> " + response.servers().get(0).get("processId"), true);
                    builder.addField("ServerStatus", "> " + response.servers().get(0).get("serverStatus"), true);
                } else {
                    if (response.servers().size() == 0) {
                        builder.setDescription("> No servers found");
                    } else {
                        for (int i = 0; i < response.servers().size(); i++) {
                            HashMap<String, Object> data = response.servers().get(i);
                            builder.addField((String) data.get("name"), "> Id: " + data.get("id") + "\n> Template: " + data.get("template") + "\n> MaxPlayers: " + data.get("maxPlayers") + "\n> ProcessId: " + data.get("processId") + "\n> ServerStatus: " + data.get("serverStatus"), true);
                        }
                    }
                }
            }

            builder.setFooter("Response time (RestAPI): " + Utils.calculateTime(startTime) + "ms");
            event.getHook().editOriginal(MessageEditData.fromEmbeds(builder.build())).queue();
        });
    }
}
