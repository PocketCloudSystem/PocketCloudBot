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
import java.util.List;

public class StartCommand extends Command {

    public StartCommand() {
        super(Commands.slash("start", "Start a server")
                .addOption(OptionType.STRING, "template", "Name of the template", true)
                .addOption(OptionType.INTEGER, "count", "Count for the amount of servers that will be started")
        );
    }

    @Override
    public void execute(SlashCommandInteractionEvent event, SlashCommandInteraction interaction, List<OptionMapping> options, long startTime) {
        String template = event.getOption("template", OptionMapping::getAsString);
        Integer count = event.getOption("count", OptionMapping::getAsInt);
        if (count == null) count = 1;

        CloudAPI.startServer(template, count).thenAccept(response -> {
            if (response.statusCode() == -1) {
                event.getHook().editOriginal(MessageEditData.fromEmbeds(Utils.timedOut())).queue();
                return;
            }

            if (response.statusCode() != 200) {
                event.getHook().editOriginal(MessageEditData.fromEmbeds(Utils.errorOccurred(response.statusCode()))).queue();
                return;
            }

            EmbedBuilder builder = new EmbedBuilder();
            if (response.error()) {
                builder.setTitle("Failed | Start server");
                builder.setColor(Color.RED);
                builder.setDescription(response.message());
            } else {
                builder.setTitle("Success | Start server");
                builder.setColor(Color.GREEN);
                builder.setDescription(response.message());
            }

            builder.setFooter("Response time (RestAPI): " + Utils.calculateTime(startTime) + "ms");
            event.getHook().editOriginal(MessageEditData.fromEmbeds(builder.build())).queue();
        });
    }
}
