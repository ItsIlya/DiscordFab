package io.github.hansi132.discordfab.discordbot.commands.argument;

import io.github.hansi132.discordfab.discordbot.api.command.BotCommandSource;
import com.google.common.collect.Lists;
import com.mojang.authlib.GameProfile;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;

import java.util.Collection;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MinecraftPlayerArgument implements ArgumentType<GameProfile> {
    private static final Pattern USERNAME_PATTERN = Pattern.compile("/[a-zA-Z][a-zA-Z0-9-_]{3,16}/gi");
    private static final Pattern UUID_PATTERN = Pattern.compile("[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}");
    private static final List<String> EXAMPLES = Lists.newArrayList("Player", "dd12be42-52a9-4a91-a8a1-11c01849e498");
    private static final SimpleCommandExceptionType INVALID_PLAYER = new SimpleCommandExceptionType(() -> "Invalid Player!");
    private static final SimpleCommandExceptionType INVALID_USERNAME = new SimpleCommandExceptionType(() -> "Invalid Username!");
    private static final SimpleCommandExceptionType INVALID_UUID = new SimpleCommandExceptionType(() -> "Invalid UUID!");

    public static MinecraftPlayerArgument player() {
        return new MinecraftPlayerArgument();
    }

    public static GameProfile getProfile(CommandContext<BotCommandSource> context, String name) {
        return context.getArgument(name, GameProfile.class);
    }

    public static String getUsername(CommandContext<BotCommandSource> context, String name) throws CommandSyntaxException {
        GameProfile profile = context.getArgument(name, GameProfile.class);
        if (profile.getName() == null) {
            throw INVALID_USERNAME.create();
        }

        return profile.getName();
    }

    public static UUID getId(CommandContext<BotCommandSource> context, String name) throws CommandSyntaxException {
        GameProfile profile = context.getArgument(name, GameProfile.class);
        if (profile.getId() == null) {
            throw INVALID_UUID.create();
        }

        return profile.getId();
    }

    @Override
    public GameProfile parse(StringReader reader) {
        final String string = reader.readUnquotedString();
        Matcher uuidMatcher = UUID_PATTERN.matcher(string);
        if (uuidMatcher.find()) {
            return new GameProfile(UUID.fromString(uuidMatcher.group(1)), "");
        }

        return new GameProfile(null, string);
    }

    @Override
    public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
        return builder.buildFuture();
    }

    @Override
    public Collection<String> getExamples() {
        return EXAMPLES;
    }
}
