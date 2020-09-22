package io.github.hansi132.discordfab;

import io.github.hansi132.discordfab.discordbot.commands.EssentialsDiscordLinkCommand;
import io.github.hansi132.discordfab.discordbot.config.DataConfig;
import com.github.hansi132.discordfab.discordbot.integration.*;
import io.github.hansi132.discordfab.discordbot.util.Constants;
import io.github.hansi132.discordfab.discordbot.integration.DiscordBroadcaster;
import io.github.hansi132.discordfab.discordbot.integration.PlayerJoinBroadcaster;
import io.github.hansi132.discordfab.discordbot.integration.PlayerLeaveBroadcaster;
import net.fabricmc.api.DedicatedServerModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import org.kilocraft.essentials.api.KiloEssentials;
import org.kilocraft.essentials.api.KiloServer;

import java.io.File;
import java.util.HashMap;

public class JDiscordFabMod implements DedicatedServerModInitializer {

    @Override
    public void onInitializeServer() {
        final File CONFIG_FILE = Constants.CONFIG_PATH.toFile();
        if (!CONFIG_FILE.exists()) {
            CONFIG_FILE.mkdirs();
        }

        DataConfig config = new DataConfig(
                new HashMap<String, Object>() {{
                    this.put("token", "*paste bot token here*");
                    this.put("webhook_url", "*paste webhook_url here*");
                    this.put("broadcastEnable", "*True or false*");
                    this.put("broadcastChannel", "*Paste what channel to broadcast to here*");
                    this.put("discordBroadcaster", "*Webhook url to cast Minecraft chat to*");
                    this.put("databaseUser", "*Specify the user of the database*");
                    this.put("databasePassword", "*Specify the password of the database*");
                    this.put("database", "*Specify the database to be used*");
                    this.put("guild", "*Specify the guild id*");
                }}
        );

        JDiscordFab fab = new JDiscordFab(config);

        ServerLifecycleEvents.SERVER_STARTED.register((server -> {
            KiloServer.getServer().registerEvent(new DiscordBroadcaster());
            KiloServer.getServer().registerEvent(new PlayerJoinBroadcaster());
            KiloServer.getServer().registerEvent(new PlayerLeaveBroadcaster());

            KiloEssentials.getInstance().getCommandHandler().register(
                    new EssentialsDiscordLinkCommand("link", new String[]{"discord_link"})
            );
        }));

        ServerLifecycleEvents.SERVER_STOPPED.register((server) -> fab.shutdown());
    }
}
