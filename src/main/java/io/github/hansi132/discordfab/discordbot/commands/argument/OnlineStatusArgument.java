package io.github.hansi132.discordfab.discordbot.commands.argument;

import io.github.hansi132.discordfab.discordbot.api.command.BotCommandSource;
import com.google.common.collect.Lists;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import net.dv8tion.jda.api.OnlineStatus;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class OnlineStatusArgument implements ArgumentType<OnlineStatus> {
    private static final List<String> EXAMPLES = Lists.newArrayList("online", "idle");
    private static final DynamicCommandExceptionType INVALID_STATUS_EXCEPTION = new DynamicCommandExceptionType((object) ->
            () -> "**" + object + "** is not a valid Online Status"
    );

    public static OnlineStatusArgument onlineStatus() {
        return new OnlineStatusArgument();
    }

    public static OnlineStatus getStatus(CommandContext<BotCommandSource> context, String name) {
        return context.getArgument(name, OnlineStatus.class);
    }

    @Override
    public OnlineStatus parse(StringReader reader) throws CommandSyntaxException {
        final String string = reader.readUnquotedString();
        OnlineStatus status = OnlineStatus.fromKey(string);
        if (status == null) {
            throw INVALID_STATUS_EXCEPTION.create(string);
        }

        return status;
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
