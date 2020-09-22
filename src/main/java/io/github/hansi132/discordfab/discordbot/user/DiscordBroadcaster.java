package io.github.hansi132.discordfab.discordbot.user;

import club.minnced.discord.webhook.WebhookClient;
import club.minnced.discord.webhook.WebhookClientBuilder;
import club.minnced.discord.webhook.send.AllowedMentions;
import club.minnced.discord.webhook.send.WebhookMessage;
import io.github.hansi132.discordfab.JDiscordFab;
import org.jetbrains.annotations.NotNull;

public class DiscordBroadcaster {
    private static final JDiscordFab DISCORD_FAB = JDiscordFab.getInstance();
    private final WebhookClient client;

    public DiscordBroadcaster() {
        final String url = DISCORD_FAB.getDataConfig().getProperty("discordBroadcaster");
        WebhookClientBuilder builder = new WebhookClientBuilder(url);
        builder.setAllowedMentions(AllowedMentions.none());
        this.client = builder.build();
    }

    public void send(@NotNull WebhookMessage message) {
        this.client.send(message);
    }

    public WebhookClient getClient() {
        return this.client;
    }
}
