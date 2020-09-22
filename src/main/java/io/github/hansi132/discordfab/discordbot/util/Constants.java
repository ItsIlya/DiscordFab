package io.github.hansi132.discordfab.discordbot.util;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DecimalFormat;

public class Constants {
    public static final DecimalFormat DECIMAL_FORMAT = new DecimalFormat("##.##");
    public static final String WORKING_DIR = System.getProperty("user.dir");
    public static final Path WORKING_PATH = Paths.get(WORKING_DIR);
    public static final Path CONFIG_PATH = WORKING_PATH.resolve("DiscordFab");
}
