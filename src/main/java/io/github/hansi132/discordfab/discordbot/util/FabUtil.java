package io.github.hansi132.discordfab.discordbot.util;

import org.jetbrains.annotations.NotNull;

import java.math.BigInteger;
import java.util.UUID;

public class FabUtil {
    public static UUID uuidFromShortenedUuidString(@NotNull final String id) {
        return new UUID(
                new BigInteger(id.substring(0, 16), 16).longValue(),
                new BigInteger(id.substring(16), 16).longValue()
        );
    }
}
