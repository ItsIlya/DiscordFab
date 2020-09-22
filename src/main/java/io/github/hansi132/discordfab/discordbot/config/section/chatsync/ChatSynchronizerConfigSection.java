package io.github.hansi132.discordfab.discordbot.config.section.chatsync;

import ninja.leaping.configurate.objectmapping.Setting;
import ninja.leaping.configurate.objectmapping.serialize.ConfigSerializable;

@ConfigSerializable
public class ChatSynchronizerConfigSection {

    @Setting(value = "toDiscord", comment = "Enables chat synchronization to Discord")
    public boolean toDiscord = true;

    @Setting(value = "toMinecraft", comment = "Enables chat synchronization to Minecraft")
    public boolean toMinecraft = true;

    @Setting(value = "chatChannelId", comment = "The Id for the Channel you want to be the" +
            " synchronized channel between the game and discord")
    public long chatChannelId = 123456789101112131L;

    @Setting(value = "defaultAvatarUrl", comment = "The Avatar URL to use if the User isn't linked, " +
            "leave it empty to use their Minecraft Avatar instead")
    public String defaultAvatarUrl = "";

    @Setting(value = "messages")
    public ChatSyncMessagesConfigSection messages = new ChatSyncMessagesConfigSection();

    @Setting("minecraftAvatarRenderOptions")
    public MCAvatarRenderOptionsConfigSection renderOptions = new MCAvatarRenderOptionsConfigSection();

    public boolean isEnabled() {
        return toDiscord || toMinecraft;
    }
}
