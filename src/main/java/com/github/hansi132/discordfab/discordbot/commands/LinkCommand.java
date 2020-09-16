package com.github.hansi132.discordfab.discordbot.commands;

import com.github.hansi132.discordfab.discordbot.api.command.BotCommandSource;
import com.github.hansi132.discordfab.discordbot.api.command.CommandCategory;
import com.github.hansi132.discordfab.discordbot.api.command.DiscordFabCommand;
import com.github.hansi132.discordfab.discordbot.integration.UserSynchronizer;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.context.CommandContext;
import net.dv8tion.jda.api.entities.MessageChannel;
import org.jetbrains.annotations.NotNull;

public class LinkCommand extends DiscordFabCommand {

    public LinkCommand(@NotNull CommandCategory category, @NotNull String label) {
        super(category, label);
        this.withDescription("Links your account.");

        this.argBuilder.then(
                argument("linkKey", IntegerArgumentType.integer(1000, 9999))
                        .executes(this::execute)
        );
    }

    private int execute(final CommandContext<BotCommandSource> ctx) {
        BotCommandSource src = ctx.getSource();
        int linkKey = IntegerArgumentType.getInteger(ctx, "linkKey");
        MessageChannel channel = src.getChannel();

        if (!UserSynchronizer.isLinkCode(String.valueOf(linkKey))) {
            src.sendError(DISCORD_FAB.getConfig().messages.invalid_link_key).queue();
            return FAILED;
        }

        UserSynchronizer.sync(null, channel, src.getUser(), linkKey);
        return SUCCESS;
    }

}
