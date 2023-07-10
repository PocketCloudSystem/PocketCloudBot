package de.pocketcloud.bot.listener;

import de.pocketcloud.bot.Bot;
import de.pocketcloud.bot.command.Command;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.session.ReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

public class EventListener extends ListenerAdapter {

    @Override
    public void onReady(@NotNull ReadyEvent event) {
        if (Bot.getInstance().getCommandManager().getCommands().size() == 0) return;
        event.getJDA().updateCommands().addCommands(Bot.getInstance().getCommandManager().getCommands().stream().map(Command::getCommandData).toList()).queue();
    }

    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {
        Bot.getInstance().getCommandManager().execute(event);
    }
}
