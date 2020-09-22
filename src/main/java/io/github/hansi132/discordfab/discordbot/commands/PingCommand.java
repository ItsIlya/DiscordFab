package io.github.hansi132.discordfab.discordbot.commands;

import io.github.hansi132.discordfab.discordbot.api.command.BotCommandSource;
import io.github.hansi132.discordfab.discordbot.api.command.CommandCategory;
import io.github.hansi132.discordfab.discordbot.api.command.DiscordFabCommand;
import com.mojang.brigadier.context.CommandContext;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.TimeUnit;

public class PingCommand extends DiscordFabCommand {

    public PingCommand(@NotNull CommandCategory category, @NotNull String label) {
        super(category, label);
        this.withDescription("Gives you the ping between the bot and discord servers.");
        this.argBuilder.executes(this::execute);
    }

    private int execute(final CommandContext<BotCommandSource> ctx) {
        BotCommandSource src = ctx.getSource();
        final double ping = src.getJDA().getGatewayPing();
        final double restPing = src.getJDA().getRestPing().complete();

        src.sendFeedback("**Pong!**").queueAfter(2, TimeUnit.SECONDS, (message) ->
                message.editMessage("**Pong!** The Gateway latency is **" + ping +
                        "ms** and the REST API latency is **" + restPing + "**ms").queue()
        );

        return (int) ping;

    }

}
