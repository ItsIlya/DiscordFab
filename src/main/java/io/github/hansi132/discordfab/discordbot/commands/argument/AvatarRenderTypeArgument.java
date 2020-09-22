package io.github.hansi132.discordfab.discordbot.commands.argument;

import io.github.hansi132.discordfab.discordbot.api.command.BotCommandSource;
import io.github.hansi132.discordfab.discordbot.util.MinecraftAvatar;
import com.google.common.collect.Lists;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class AvatarRenderTypeArgument implements ArgumentType<MinecraftAvatar.RenderType> {
    private static final List<String> EXAMPLES = Lists.newArrayList("avatar", "body");
    private static final DynamicCommandExceptionType INVALID_RENDER_TYPE_EXCEPTION = new DynamicCommandExceptionType((object) ->
            () -> "**" + object + "** is not a valid Render Type"
    );

    public static AvatarRenderTypeArgument renderType() {
        return new AvatarRenderTypeArgument();
    }

    public static MinecraftAvatar.RenderType getRenderType(CommandContext<BotCommandSource> context, String name) {
        return context.getArgument(name, MinecraftAvatar.RenderType.class);
    }

    @Override
    public MinecraftAvatar.RenderType parse(StringReader reader) throws CommandSyntaxException {
        final String string = reader.readUnquotedString();
        final MinecraftAvatar.RenderType renderType = MinecraftAvatar.RenderType.getByName(string);
        if (renderType == null) {
            throw INVALID_RENDER_TYPE_EXCEPTION.create(string);
        }

        return renderType;
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
