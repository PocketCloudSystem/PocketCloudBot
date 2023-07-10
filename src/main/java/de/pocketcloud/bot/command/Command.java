package de.pocketcloud.bot.command;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.SlashCommandInteraction;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;

import java.util.List;

public abstract class Command {

    private final CommandData commandData;

    public Command(CommandData commandData) {
        this.commandData = commandData;
    }

    abstract public void execute(SlashCommandInteractionEvent event, SlashCommandInteraction interaction, List<OptionMapping> options, long startTime);

    public CommandData getCommandData() {
        return commandData;
    }
}
