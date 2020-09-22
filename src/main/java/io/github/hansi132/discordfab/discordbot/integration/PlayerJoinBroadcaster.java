package io.github.hansi132.discordfab.discordbot.integration;

import io.github.hansi132.discordfab.JDiscordFab;
import org.jetbrains.annotations.NotNull;
import org.kilocraft.essentials.api.event.EventHandler;
import org.kilocraft.essentials.api.event.player.PlayerConnectedEvent;

public class PlayerJoinBroadcaster implements EventHandler<PlayerConnectedEvent> {

    @Override
    public void handle(@NotNull PlayerConnectedEvent playerConnectedEvent) {
        JDiscordFab.getInstance().getChatSynchronizer().onUserJoin(playerConnectedEvent.getUser());
    }
}
