package de.pocketcloud.bot.command.impl.player;

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

public class TextCommand extends Command {

    public TextCommand() {
        super(Commands.slash("text", "Text a player").addOptions(
                new OptionData(OptionType.STRING, "player", "Name of the player", true),
                new OptionData(OptionType.STRING, "type", "Type of the text", true)
                        .addChoice("message", "MESSAGE")
                        .addChoice("popup", "POPUP")
                        .addChoice("tip", "TIP")
                        .addChoice("title", "TITLE")
                        .addChoice("action_bar", "ACTION_BAR")
                        .addChoice("toast_notification", "TOAST_NOTIFICATION"),
                new OptionData(OptionType.STRING, "text", "The text for the player", true)
        ));
    }

    @Override
    public void execute(SlashCommandInteractionEvent event, SlashCommandInteraction interaction, List<OptionMapping> options, long startTime) {
        String player = event.getOption("player", OptionMapping::getAsString);
        String type = event.getOption("type", OptionMapping::getAsString);
        String text = event.getOption("text", OptionMapping::getAsString);

        CloudAPI.textPlayer(player, type, text).thenAccept(response -> {
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
                builder.setTitle("Failed | Text player");
                builder.setColor(Color.RED);
                builder.setDescription(response.message());
            } else {
                builder.setTitle("Success | Text player");
                builder.setColor(Color.GREEN);
                builder.setDescription(response.message());
            }

            builder.setFooter("Response time (RestAPI): " + Utils.calculateTime(startTime) + "ms");
            event.getHook().editOriginal(MessageEditData.fromEmbeds(builder.build())).queue();
        });
    }
}
