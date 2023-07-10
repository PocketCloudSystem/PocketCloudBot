package de.pocketcloud.bot.command.impl.plugin;

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
import java.util.List;

public class PluginInfoCommand extends Command {

    public PluginInfoCommand() {
        super(Commands.slash("plugin-info", "Get information about a plugin")
                .addOption(OptionType.STRING, "plugin", "Name of the plugin", true)
        );
    }

    @Override
    public void execute(SlashCommandInteractionEvent event, SlashCommandInteraction interaction, List<OptionMapping> options, long startTime) {
        String plugin = event.getOption("plugin", OptionMapping::getAsString);

        CloudAPI.getPluginInformation(plugin).thenAccept(response -> {
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
                builder.setTitle("Failed | Get plugin");
                builder.setColor(Color.RED);
                builder.setDescription(response.error());
            } else {
                builder.setTitle("Success | Get plugin");
                builder.setColor(Color.GREEN);
                builder.addField("Name", "> " + response.name() + " (" + response.name() + "@" + response.version() + ")", true);
                builder.addField("Main", "> " + response.main(), true);
                builder.addField("Version", "> " + response.version(), true);
                builder.addField("Authors", "> " + (response.authors().isEmpty() ? "No authors" : String.join(", ", response.authors())), true);
                builder.addField("Description", "> " + (response.description() == null ? "No description" : (response.description().trim().isBlank() ? "No description" : response.description())), true);
                builder.addField("Enabled", "> " + (response.enabled() ? "Yes" : "No"), true);
            }

            builder.setFooter("Response time (RestAPI): " + Utils.calculateTime(startTime) + "ms");
            event.getHook().editOriginal(MessageEditData.fromEmbeds(builder.build())).queue();
        });
    }
}
