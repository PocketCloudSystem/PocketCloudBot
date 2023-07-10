package de.pocketcloud.bot.command.impl.template;

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

public class TemplateInfoCommand extends Command {

    public TemplateInfoCommand() {
        super(Commands.slash("template-info", "Get information about a template")
                .addOption(OptionType.STRING, "template", "Name of the template", true)
        );
    }

    @Override
    public void execute(SlashCommandInteractionEvent event, SlashCommandInteraction interaction, List<OptionMapping> options, long startTime) {
        String template = event.getOption("template", OptionMapping::getAsString);

        CloudAPI.getTemplateInformation(template).thenAccept(response -> {
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
                builder.setTitle("Failed | Get template");
                builder.setColor(Color.RED);
                builder.setDescription(response.error());
            } else {
                builder.setTitle("Success | Get template");
                builder.setColor(Color.GREEN);
                builder.addField("Name", "> " + response.name(), true);
                builder.addField("isLobby", "> " + (response.lobby() ? "Yes" : "No"), true);
                builder.addField("isMaintenance", "> " + (response.maintenance() ? "Yes" : "No"), true);
                builder.addField("isStatic", "> " + (response.staticServers() ? "Yes" : "No"), true);
                builder.addField("MaxPlayerCount", "> " + response.maxPlayerCount(), true);
                builder.addField("MinServerCount", "> " + response.minServerCount(), true);
                builder.addField("MaxServerCount", "> " + response.maxServerCount(), true);
                builder.addField("isStartNewWhenFull", "> " + (response.startNewWhenFull() ? "Yes" : "No"), true);
                builder.addField("isAutoStart", "> " + (response.autoStart() ? "Yes" : "No"), true);
                builder.addField("TemplateType", "> " + response.templateType(), true);
            }

            builder.setFooter("Response time (RestAPI): " + Utils.calculateTime(startTime) + "ms");
            event.getHook().editOriginal(MessageEditData.fromEmbeds(builder.build())).queue();
        });
    }
}