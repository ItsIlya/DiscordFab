package io.github.hansi132.discordfab.discordbot.commands;

import io.github.hansi132.discordfab.discordbot.api.command.BotCommandSource;
import io.github.hansi132.discordfab.discordbot.api.command.CommandCategory;
import io.github.hansi132.discordfab.discordbot.api.command.DiscordFabCommand;
import io.github.hansi132.discordfab.discordbot.integration.UserSynchronizer;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import net.dv8tion.jda.api.entities.MessageChannel;
import org.jetbrains.annotations.NotNull;

public class LinkCommand extends DiscordFabCommand {

    public LinkCommand(@NotNull CommandCategory category, @NotNull String label) {
        super(category, label);
        this.withDescription("Links your account.");

        final RequiredArgumentBuilder<BotCommandSource, Integer> linkKey = argument("linkKey", IntegerArgumentType.integer(1000, 9999))
                .executes(this::execute);
        this.argBuilder.then(linkKey);
    }

    private int execute(final CommandContext<BotCommandSource> ctx) {
        BotCommandSource src = ctx.getSource();
        int linkKey = IntegerArgumentType.getInteger(ctx, "linkKey");
        MessageChannel channel = src.getChannel();

        if (!UserSynchronizer.isLinkCode(String.valueOf(linkKey))) {
            channel.sendMessage(DISCORD_FAB.getConfig().messages.invalid_link_key).queue();
            return 0;
        } else {
            UserSynchronizer.sync(null, channel, src.getUser(), linkKey);
            return 1;
        }


    }


}
