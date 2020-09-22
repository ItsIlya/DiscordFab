package io.github.hansi132.discordfab.discordbot.api.command;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public enum CommandCategory {
    DEBUG("Debug"),
    GENERAL("General"),
    ADMIN("Admin"),
    UTILITY("Utility"),
    HELP("Help"),
    FUN("Fun");

    private final String name;
    CommandCategory(final String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    @Nullable
    public static CommandCategory getByName(@NotNull final String name) {
        for (CommandCategory value : values()) {
            if (value.name.equalsIgnoreCase(name)) {
                return value;
            }
        }

        return null;
    }
}
