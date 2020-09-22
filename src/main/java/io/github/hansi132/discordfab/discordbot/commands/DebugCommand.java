package io.github.hansi132.discordfab.discordbot.commands;

import io.github.hansi132.discordfab.discordbot.api.command.BotCommandSource;
import io.github.hansi132.discordfab.discordbot.api.command.CommandCategory;
import io.github.hansi132.discordfab.discordbot.api.command.DiscordFabCommand;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.tree.CommandNode;
import net.dv8tion.jda.api.Permission;

import java.util.concurrent.TimeUnit;

public class DebugCommand extends DiscordFabCommand {
    public DebugCommand() {
        super(CommandCategory.DEBUG, "debug", Permission.ADMINISTRATOR);
        this.withDescription("A debug command");
        CommandNode<BotCommandSource> throwNode = literal("throw")
                .then(
                        argument("message", StringArgumentType.greedyString())
                                .executes(this::executeThrow)
                ).build();

        this.cmdNode.addChild(throwNode);
    }

    private int executeThrow(final CommandContext<BotCommandSource> ctx) {
        String message = StringArgumentType.getString(ctx, "message");
        ctx.getSource().sendFeedback("*Wait for it..*").queueAfter(2, TimeUnit.SECONDS, (msg) ->
                msg.editMessage("***BOOM!***")
        );

        throw new NullPointerException(message);
    }
}
