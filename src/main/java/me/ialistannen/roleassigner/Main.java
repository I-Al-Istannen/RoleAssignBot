package me.ialistannen.roleassigner;

import me.ialistannen.roleassigner.event.CommandListener;
import me.ialistannen.roleassigner.util.ConfigManager;
import sx.blah.discord.api.ClientBuilder;
import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.util.DiscordException;
import sx.blah.discord.util.RateLimitException;

/**
 * The main class for the bot
 */
public class Main {
    
    private Main(String token) throws DiscordException, RateLimitException {
        IDiscordClient client = new ClientBuilder()
                .withToken(token)
                .build();
        client.getDispatcher().registerListener(new CommandListener("!"));
        client.login();
    }

    public static void main(String[] args) throws Exception {
        new Main(ConfigManager.INSTANCE.getString("token"));
    }
}
