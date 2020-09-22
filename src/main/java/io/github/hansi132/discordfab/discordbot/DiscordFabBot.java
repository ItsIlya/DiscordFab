package io.github.hansi132.discordfab.discordbot;

import net.dv8tion.jda.api.hooks.EventListener;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.sharding.DefaultShardManagerBuilder;
import net.dv8tion.jda.api.sharding.ShardManager;
import org.jetbrains.annotations.NotNull;

import javax.security.auth.login.LoginException;

public class DiscordFabBot {
    private final ShardManager shardManager;

    public <E extends EventListener> DiscordFabBot(@NotNull String token, @NotNull E eventListener) throws LoginException {
        this.shardManager = DefaultShardManagerBuilder.create(
                token,
                GatewayIntent.getIntents(GatewayIntent.ALL_INTENTS))
                .addEventListeners(eventListener)
                .build();
    }

    public ShardManager getShardManager() {
        return this.shardManager;
    }

}
