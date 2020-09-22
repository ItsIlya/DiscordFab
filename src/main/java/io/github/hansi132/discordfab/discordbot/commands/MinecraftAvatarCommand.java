package io.github.hansi132.discordfab.discordbot.commands;

import io.github.hansi132.discordfab.discordbot.api.command.BotCommandSource;
import io.github.hansi132.discordfab.discordbot.api.command.CommandCategory;
import io.github.hansi132.discordfab.discordbot.api.command.DiscordFabCommand;
import io.github.hansi132.discordfab.discordbot.commands.argument.AvatarRenderTypeArgument;
import io.github.hansi132.discordfab.discordbot.commands.argument.MinecraftPlayerArgument;
import io.github.hansi132.discordfab.discordbot.util.FabUtil;
import io.github.hansi132.discordfab.discordbot.util.MinecraftAvatar;
import com.mojang.authlib.GameProfile;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.dv8tion.jda.api.EmbedBuilder;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.kilocraft.essentials.util.NameLookup;

import java.io.IOException;
import java.time.Instant;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public class MinecraftAvatarCommand extends DiscordFabCommand {
    private static final int DEFAULT_SIZE = 512;
    private static final int DEFAULT_SCALE = 10;

    public MinecraftAvatarCommand(@NotNull CommandCategory category, @NotNull String label, @Nullable String... alias) {
        super(category, label, alias);
        this.withDescription("Get someone's Minecraft Avatar!");

        RequiredArgumentBuilder<BotCommandSource, GameProfile> player = argument("player", MinecraftPlayerArgument.player())
                .executes((ctx) -> this.execute(
                        ctx,
                        MinecraftAvatar.RenderType.BODY,
                        DEFAULT_SIZE,
                        DEFAULT_SCALE,
                        true
                ));

        RequiredArgumentBuilder<BotCommandSource, MinecraftAvatar.RenderType> renderType = argument("renderType", AvatarRenderTypeArgument.renderType())
                .executes((ctx) -> this.execute(
                        ctx,
                        AvatarRenderTypeArgument.getRenderType(ctx, "renderType"),
                        DEFAULT_SIZE,
                        DEFAULT_SCALE,
                        true
                ));

        RequiredArgumentBuilder<BotCommandSource, Integer> size = argument("size", IntegerArgumentType.integer(16, 512))
                .executes((ctx) -> this.execute(
                        ctx,
                        AvatarRenderTypeArgument.getRenderType(ctx, "renderType"),
                        IntegerArgumentType.getInteger(ctx, "size"),
                        DEFAULT_SCALE,
                        true)
                );

        RequiredArgumentBuilder<BotCommandSource, Integer> scale = argument("scale", IntegerArgumentType.integer(1, 10))
                .executes((ctx) -> this.execute(
                        ctx,
                        AvatarRenderTypeArgument.getRenderType(ctx, "renderType"),
                        IntegerArgumentType.getInteger(ctx, "size"),
                        IntegerArgumentType.getInteger(ctx, "scale"),
                        true
                ));


        RequiredArgumentBuilder<BotCommandSource, Boolean> overlay = argument("overlay", BoolArgumentType.bool())
                .executes((ctx) -> this.execute(
                        ctx,
                        AvatarRenderTypeArgument.getRenderType(ctx, "renderType"),
                        IntegerArgumentType.getInteger(ctx, "size"),
                        IntegerArgumentType.getInteger(ctx, "scale"),
                        BoolArgumentType.getBool(ctx, "overlay"))
                );

        scale.then(overlay);
        size.then(scale);
        renderType.then(size);
        player.then(renderType);
        this.argBuilder.then(player);
    }


    private int execute(final CommandContext<BotCommandSource> ctx,
                        final MinecraftAvatar.RenderType renderType,
                        int size, int scale, boolean overlay) throws CommandSyntaxException {
        final BotCommandSource src = ctx.getSource();
        final String username = MinecraftPlayerArgument.getUsername(ctx, "player");

        CompletableFuture.runAsync(() -> {
            GameProfile profile;

            try {
                final String id = NameLookup.getPlayerUUID(username);

                if (id == null) {
                    src.sendFeedback("Invalid username!").queue();
                    return;
                }

                final UUID uuid = FabUtil.uuidFromShortenedUuidString(id);
                profile = new GameProfile(uuid, username);
            } catch (IOException e) {
                src.sendFeedback("Invalid username!").queue();
                return;
            }

            src.sendFeedback(
                    new EmbedBuilder()
                            .setTitle("Skin " + renderType.getName() + " of " + profile.getName())
                            .setAuthor(src.getDisplayName(), MinecraftAvatar.API_URL, src.getUser().getAvatarUrl())
                            .setColor(DISCORD_FAB.getEmbedUtil().getDefaultColor())
                            .setTimestamp(Instant.now())
                            .setFooter("Powered by: crafatar.com")
                            .setImage(
                                    MinecraftAvatar.generateUrl(
                                            profile.getId(), renderType, MinecraftAvatar.RenderType.Model.DEFAULT,
                                            size, scale, overlay
                                    )
                            )
                            .build()
            ).queue();
        });

        return AWAIT;
    }

}
