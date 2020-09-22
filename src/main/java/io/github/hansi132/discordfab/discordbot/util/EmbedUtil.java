package io.github.hansi132.discordfab.discordbot.util;

import io.github.hansi132.discordfab.JDiscordFab;
import io.github.hansi132.discordfab.discordbot.config.section.DefaultEmbedConfigSection;

import java.awt.*;

public class EmbedUtil {
    private static final JDiscordFab DISCORD_FAB = JDiscordFab.getInstance();
    private static final DefaultEmbedConfigSection CONFIG = DISCORD_FAB.getConfig().defaultEmbed;
    private final Color DEFAULT_COLOR;
    private final String AUTHOR_LINK, AUTHOR_ICON_URL;

    public EmbedUtil() {
        DEFAULT_COLOR = Color.decode(CONFIG.color);
        AUTHOR_LINK = CONFIG.author_link;
        AUTHOR_ICON_URL = CONFIG.author_icon_url;
    }

    public Color getDefaultColor() {
        return DEFAULT_COLOR;
    }

    public String getAuthorLink() {
        return AUTHOR_LINK;
    }

    public String getAuthorIconURL() {
        return AUTHOR_ICON_URL;
    }
}
