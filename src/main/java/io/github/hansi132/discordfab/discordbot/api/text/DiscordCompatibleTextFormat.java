package io.github.hansi132.discordfab.discordbot.api.text;

import org.jetbrains.annotations.NotNull;
import org.kilocraft.essentials.api.text.TextFormat;

public enum DiscordCompatibleTextFormat {
    BOLD(TextFormat.BOLD, "**"),
    ITALIC(TextFormat.ITALIC, "*"),
    STRIKE_THROUGH(TextFormat.STRIKETHROUGH, "~~"),
    UNDERLINE(TextFormat.UNDERLINE, "__");

    private final TextFormat minecraftFormat;
    private final String discordFormat;
    DiscordCompatibleTextFormat(final TextFormat minecraft, final String discord) {
        this.minecraftFormat = minecraft;
        this.discordFormat = discord;
    }

    public static String clearAllDiscord(@NotNull final String string) {
        String str = string;
        for (DiscordCompatibleTextFormat value : values()) {
            str = str.replace(value.discordFormat, "");
        }
        return str;
    }

}
