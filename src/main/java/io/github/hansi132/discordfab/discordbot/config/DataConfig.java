package io.github.hansi132.discordfab.discordbot.config;

import io.github.hansi132.discordfab.discordbot.util.Constants;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class DataConfig {
    private static final Logger LOGGER = LogManager.getLogger();
    private final Properties PROPERTIES;
    private final File FILE;
    private final Map<String, Object> DEFAULTS;

    public DataConfig() {
        this(new HashMap<>());
    }

    public DataConfig(@NotNull final Map<String, Object> defaults) {
        this.PROPERTIES = new Properties();
        this.FILE = Constants.CONFIG_PATH.resolve("tokens.properties").toFile();
        this.DEFAULTS = defaults;

        if (FILE.exists()) {
            try {
                this.loadProperties();
            } catch (IOException e) {
                LOGGER.error("Could not load the data properties!", e);
            }
        } else {
            DEFAULTS.forEach(PROPERTIES::put);

            try {
                this.saveProperties();
            } catch (IOException e) {
                LOGGER.error("Could not generate the data properties!", e);
            }
        }
    }

    public void loadProperties() throws IOException {
        final InputStream in = new FileInputStream(FILE);
        PROPERTIES.load(in);
    }

    public void saveProperties() throws IOException {
        final OutputStream out = new FileOutputStream(FILE);
        PROPERTIES.store(out, "DiscordFab tokens properties");
    }

    public Properties getProperties() {
        return this.PROPERTIES;
    }

    public String getProperty(@NotNull final String key) {
        return this.PROPERTIES.getProperty(key);
    }

    public String getToken() {
        return this.PROPERTIES.getProperty("token");
    }

}
