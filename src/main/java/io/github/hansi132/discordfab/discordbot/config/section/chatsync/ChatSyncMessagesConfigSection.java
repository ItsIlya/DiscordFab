package io.github.hansi132.discordfab.discordbot.config.section.chatsync;

import ninja.leaping.configurate.objectmapping.Setting;
import ninja.leaping.configurate.objectmapping.serialize.ConfigSerializable;

@ConfigSerializable
public class ChatSyncMessagesConfigSection {

    @Setting(comment = "Variables: %name%")
    public String userJoin = "%name% has joined the game.";

    @Setting(comment = "Variables: %name%")
    public String userLeave = "%name% has left the game.";

    @Setting(comment = "Variables: %name%")
    public String prefix = "&7@&f%name% &9>&9>&f ";
}
