package com.github.hansi132.DiscordFab.discordbot.util;

import java.nio.file.Path;
import java.nio.file.Paths;

public class Variables {
    public static final String WORKING_DIR = System.getProperty("user.dir");
    public static final Path WORKING_PATH = Paths.get(WORKING_DIR);
}