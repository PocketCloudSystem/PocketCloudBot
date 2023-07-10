package de.pocketcloud.bot.command;

import de.pocketcloud.bot.command.impl.CloudInfoCommand;
import de.pocketcloud.bot.command.impl.ListCommand;
import de.pocketcloud.bot.command.impl.player.KickCommand;
import de.pocketcloud.bot.command.impl.player.PlayerInfoCommand;
import de.pocketcloud.bot.command.impl.player.TextCommand;
import de.pocketcloud.bot.command.impl.plugin.PluginInfoCommand;
import de.pocketcloud.bot.command.impl.server.*;
import de.pocketcloud.bot.command.impl.template.TemplateInfoCommand;
import de.pocketcloud.bot.util.Utils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.utils.messages.MessageCreateData;

import java.awt.*;
import java.util.ArrayList;

public class CommandManager {

    private final ArrayList<Command> commands = new ArrayList<>();

    public CommandManager() {
        addCommand(new CloudInfoCommand());
        addCommand(new StartCommand());
        addCommand(new StopCommand());
        addCommand(new SaveCommand());
        addCommand(new ExecuteCommand());
        addCommand(new ServerInfoCommand());
        addCommand(new KickCommand());
        addCommand(new TextCommand());
        addCommand(new PlayerInfoCommand());
        addCommand(new TemplateInfoCommand());
        addCommand(new PluginInfoCommand());
        addCommand(new ListCommand());
    }

    public void execute(SlashCommandInteractionEvent event) {
        Command command = getCommand(event.getName());
        if (command != null) {
            if (event.getInteraction().getMember() == null) return;
            if (event.getInteraction().getMember().hasPermission(Permission.ADMINISTRATOR)) {
                event.deferReply().queue();
                command.execute(event, event.getInteraction(), event.getOptions(), Utils.milliTime());
            } else {
                event.reply(MessageCreateData.fromEmbeds(new EmbedBuilder()
                        .setTitle("Failed | No Permissions")
                        .setColor(Color.RED)
                        .setDescription("You don't have enough permissions to execute this command.")
                        .setFooter("Required: ADMINISTRATOR")
                        .build()
                )).queue();
            }
        }
    }

    public void addCommand(Command command) {
        commands.add(command);
    }

    public void removeCommand(Command command) {
        commands.remove(command);
    }

    public void removeCommand(String name) {
        Command command = getCommand(name);
        if (command != null) removeCommand(command);
    }

    public Command getCommand(String name) {
        return commands.stream().filter(command -> command.getCommandData().getName().equalsIgnoreCase(name)).findFirst().orElse(null);
    }

    public ArrayList<Command> getCommands() {
        return commands;
    }
}
