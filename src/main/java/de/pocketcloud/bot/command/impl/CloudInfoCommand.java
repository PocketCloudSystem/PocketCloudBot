package de.pocketcloud.bot.command.impl;

import de.pocketcloud.bot.api.CloudAPI;
import de.pocketcloud.bot.command.Command;
import de.pocketcloud.bot.util.Utils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.SlashCommandInteraction;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.utils.messages.MessageEditData;

import java.awt.*;
import java.util.List;

public class CloudInfoCommand extends Command {

    public CloudInfoCommand() {
        super(Commands.slash("cloud-info", "Information about the cloud"));
    }

    @Override
    public void execute(SlashCommandInteractionEvent event, SlashCommandInteraction interaction, List<OptionMapping> options, long startTime) {
        CloudAPI.getCloudInformation().thenAccept(response -> {
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
            builder.setTitle("Information about cloud");
            builder.addField("Version", "> " + response.version(), true);
            builder.addField("Developer", "> " + String.join(", ", response.developer()), true);
            builder.addField("Templates", (response.templates().isEmpty() ? "> No templates" : "> " + String.join("\n> ", response.templates())), true);
            builder.addField("Servers", (response.runningServers().isEmpty() ? "> No servers" : "> " + String.join("\n> ", response.runningServers())), true);
            builder.addField("Players", (response.players().isEmpty() ? "> No players" : "> " + String.join("\n> ", response.players())), true);
            builder.addField("Loaded Plugins", (response.loadedPlugins().isEmpty() ? "> No plugins" : "> " + String.join("\n> ", response.loadedPlugins())), true);
            builder.addField("Enabled Plugins", (response.enabledPlugins().isEmpty() ? "> No enabled plugins" : "> " + String.join("\n> ",  response.enabledPlugins())), true);
            builder.addField("Disabled Plugins", (response.disabledPlugins().isEmpty() ? "> No disabled plugins" : "> " + String.join("\n> ", response.disabledPlugins())), true);
            builder.addField("Network address", "> " + response.networkAddress(), true);
            builder.setFooter("Response time (RestAPI): " + Utils.calculateTime(startTime) + "ms");
            event.getHook().editOriginal(MessageEditData.fromEmbeds(builder.build())).queue();
        });
    }
}
