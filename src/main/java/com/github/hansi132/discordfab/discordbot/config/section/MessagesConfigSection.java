package com.github.hansi132.discordfab.discordbot.config.section;

import ninja.leaping.configurate.objectmapping.Setting;
import ninja.leaping.configurate.objectmapping.serialize.ConfigSerializable;

@ConfigSerializable
public class MessagesConfigSection {

    @Setting("commandNoPermission")
    public String command_parse_no_permission = "Insufficient permission! You can't use that command!";

    @Setting(value = "commandParseHelp", comment = "Variables: $command$")
    public String command_parse_help = "Use '$command$' for help";

}