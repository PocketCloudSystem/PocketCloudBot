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

public class PlayerInfoCommand extends Command {

    public PlayerInfoCommand() {
        super(Commands.slash("player-info", "Get information about a player")
                .addOption(OptionType.STRING, "player", "Name of the player", true)
        );
    }

    @Override
    public void execute(SlashCommandInteractionEvent event, SlashCommandInteraction interaction, List<OptionMapping> options, long startTime) {
        String player = event.getOption("player", OptionMapping::getAsString);

        CloudAPI.getPlayerInformation(player).thenAccept(response -> {
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
                builder.setTitle("Failed | Get player");
                builder.setColor(Color.RED);
                builder.setDescription(response.error());
            } else {
                builder.setTitle("Success | Get player");
                builder.setColor(Color.GREEN);
                builder.addField("Name", "> " + response.name(), true);
                builder.addField("Xbox User Id", "> ||" + response.xboxUserId() + "|| (SPOILER)", true);
                builder.addField("Unique Id", "> ||" + response.uniqueId() + "|| (SPOILER)", true);
                builder.addField("Current Server", "> " + (response.currentServer() == null ? "NULL" : response.currentServer()), true);
                builder.addField("Current Proxy", "> " + (response.currentProxy() == null ? "NULL" : response.currentProxy()), true);
            }

            builder.setFooter("Response time (RestAPI): " + Utils.calculateTime(startTime) + "ms");
            event.getHook().editOriginal(MessageEditData.fromEmbeds(builder.build())).queue();
        });
    }
}
