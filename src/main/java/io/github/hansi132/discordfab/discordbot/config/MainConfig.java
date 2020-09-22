package io.github.hansi132.discordfab.discordbot.config;

import io.github.hansi132.discordfab.discordbot.config.section.DefaultEmbedConfigSection;
import io.github.hansi132.discordfab.discordbot.config.section.MessagesConfigSection;
import io.github.hansi132.discordfab.discordbot.config.section.UserSyncConfigSection;
import io.github.hansi132.discordfab.discordbot.config.section.chatsync.ChatSynchronizerConfigSection;
import ninja.leaping.configurate.objectmapping.Setting;
import ninja.leaping.configurate.objectmapping.serialize.ConfigSerializable;

@ConfigSerializable
public class MainConfig {
    public static final String HEADER = "DiscordFab! Main Configuration File\n" +
            "Licensed Under the MIT License, Copyright (c) 2020 KiloCraft\n" +
            "DiscordFab is using HOCON for its configuration files\n learn more about it here: " +
            "https://docs.spongepowered.org/stable/en/server/getting-started/configuration/hocon.html" +
            "\nYou can use Color Codes in string parameters, the character is \"&\" " +
            "More info at: https://minecraft.tools/en/color-code.php \ne.g: \"&eThe Yellow Thing\" will be yellow";

    @Setting(value = "prefix", comment = "The prefix you use for the commands, Default = \"k!\"")
    public String prefix = "k!";

    @Setting(value = "serverIp", comment = "The IP of the Minecraft Server")
    public String serverIp = "50kilo.org";

    @Setting("chatSynchronization")
    public ChatSynchronizerConfigSection chatSynchronizer = new ChatSynchronizerConfigSection();

    @Setting("defaultEmbed")
    public DefaultEmbedConfigSection defaultEmbed = new DefaultEmbedConfigSection();

    @Setting("messages")
    public MessagesConfigSection messages = new MessagesConfigSection();

    @Setting("userSynchronizer")
    public UserSyncConfigSection userSync = new UserSyncConfigSection();

}
