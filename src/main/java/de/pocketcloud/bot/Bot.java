package de.pocketcloud.bot;

import de.pocketcloud.bot.api.CloudAPI;
import de.pocketcloud.bot.command.CommandManager;
import de.pocketcloud.bot.config.Config;
import de.pocketcloud.bot.listener.EventListener;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Bot {

    private static Bot instance;
    private final ExecutorService threadPool = Executors.newCachedThreadPool();
    private final Config config;
    private final CommandManager commandManager;
    private final JDA jda;

    public Bot() {
        instance = this;

        config = new Config();
        commandManager = new CommandManager();

        jda = JDABuilder.createDefault(config.getBotToken().token())
                .addEventListeners(new EventListener())
                .build();

        try {
            jda.awaitReady();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static Bot getInstance() {
        return instance;
    }

    public ExecutorService getThreadPool() {
        return threadPool;
    }

    public Config getConfig() {
        return config;
    }

    public CommandManager getCommandManager() {
        return commandManager;
    }

    public JDA getJDA() {
        return jda;
    }
}

