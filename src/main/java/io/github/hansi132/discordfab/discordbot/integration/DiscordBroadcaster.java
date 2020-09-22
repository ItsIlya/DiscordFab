package io.github.hansi132.discordfab.discordbot.integration;

import io.github.hansi132.discordfab.JDiscordFab;
import org.jetbrains.annotations.NotNull;
import org.kilocraft.essentials.api.event.EventHandler;
import org.kilocraft.essentials.api.event.player.PlayerOnChatMessageEvent;
import org.kilocraft.essentials.chat.ServerChat;

public class DiscordBroadcaster implements EventHandler<PlayerOnChatMessageEvent> {

    @Override
    public void handle(@NotNull PlayerOnChatMessageEvent event) {
        if (event.getChannel() == ServerChat.Channel.PUBLIC && JDiscordFab.getInstance().getConfig().chatSynchronizer.toDiscord) {
            JDiscordFab.getInstance().getChatSynchronizer().onGameChat(event.getUser(), event.getMessage());
        }
    }
}
