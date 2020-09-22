package io.github.hansi132.discordfab.discordbot.integration;

import io.github.hansi132.discordfab.JDiscordFab;
import org.jetbrains.annotations.NotNull;
import org.kilocraft.essentials.api.event.EventHandler;
import org.kilocraft.essentials.api.event.player.PlayerDisconnectEvent;

public class PlayerLeaveBroadcaster implements EventHandler<PlayerDisconnectEvent> {

    @Override
    public void handle(@NotNull PlayerDisconnectEvent playerDisconnectEvent) {
        JDiscordFab.getInstance().getChatSynchronizer().onUserLeave(playerDisconnectEvent.getUser());
    }

}
