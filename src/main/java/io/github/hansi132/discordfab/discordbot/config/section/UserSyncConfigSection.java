package io.github.hansi132.discordfab.discordbot.config.section;

import ninja.leaping.configurate.objectmapping.Setting;
import ninja.leaping.configurate.objectmapping.serialize.ConfigSerializable;

@ConfigSerializable
public class UserSyncConfigSection {

    @Setting(value = "linkedRoleName", comment = "Name of the Linked Role")
    public long linkedRoleId = 123456789101112131L;

    @Setting(value = "syncDisplayName", comment = "Syncs the in-game display name with discord")
    public boolean syncDisplayName = true;

}
