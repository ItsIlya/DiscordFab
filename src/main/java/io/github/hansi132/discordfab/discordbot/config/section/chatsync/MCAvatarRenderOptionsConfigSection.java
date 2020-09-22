package io.github.hansi132.discordfab.discordbot.config.section.chatsync;

import ninja.leaping.configurate.objectmapping.Setting;
import ninja.leaping.configurate.objectmapping.serialize.ConfigSerializable;

@ConfigSerializable
public class MCAvatarRenderOptionsConfigSection {
    @Setting(value = "showOverlay", comment = "If true, the skin overlay will be rendered on top of it")
    public boolean showOverlay = true;

    @Setting(value = "renderType", comment = "Types: \"avatar\", \"head\", \"body\"")
    public String renderType = "avatar";

    @Setting(value = "size", comment = "The size of the Avatar, Default: 256, min: 16 max: 512")
    public int size = 256;

    @Setting(value = "scale", comment = "The scale factor for renders, Default: 10, min: 1 max: 10")
    public int scale = 10;
}
