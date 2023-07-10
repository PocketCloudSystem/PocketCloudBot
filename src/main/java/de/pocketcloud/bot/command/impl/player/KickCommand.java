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
import net.dv8tion.jda.api.utils.messages.MessageEditData;

import java.awt.*;
import java.util.List;

public class KickCommand extends Command {

    public KickCommand() {
        super(Commands.slash("kick", "Kick a player")
                .addOption(OptionType.STRING, "player", "Name of the player", true, false)
                .addOption(OptionType.STRING, "reason", "Reason for the kick", false, false)
        );
    }

    @Override
    public void execute(SlashCommandInteractionEvent event, SlashCommandInteraction interaction, List<OptionMapping> options, long startTime) {
        String player = interaction.getOption("player", OptionMapping::getAsString);
        String reason = interaction.getOption("reason", OptionMapping::getAsString);
        if (reason == null) reason = "";

        CloudAPI.kickPlayer(player, reason).thenAccept(response -> {
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
                builder.setTitle("Failed | Player kick");
                builder.setColor(Color.RED);
                builder.setDescription(response.message());
            } else {
                builder.setTitle("Success | Player kick");
                builder.setColor(Color.GREEN);
                builder.setDescription(response.message());
            }

            builder.setFooter("Response time (RestAPI): " + Utils.calculateTime(startTime) + "ms");
            event.getHook().editOriginal(MessageEditData.fromEmbeds(builder.build())).queue();
        });
    }
}
