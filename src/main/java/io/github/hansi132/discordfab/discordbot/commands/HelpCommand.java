package io.github.hansi132.discordfab.discordbot.commands;

import io.github.hansi132.discordfab.JDiscordFab;
import io.github.hansi132.discordfab.discordbot.api.command.BotCommandSource;
import io.github.hansi132.discordfab.discordbot.api.command.CommandCategory;
import io.github.hansi132.discordfab.discordbot.api.command.DiscordFabCommand;
import com.google.common.collect.Iterables;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.ParseResults;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import com.mojang.brigadier.tree.CommandNode;
import net.dv8tion.jda.api.EmbedBuilder;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.util.Map;

public class HelpCommand extends DiscordFabCommand {
    private static final Color EMBED_COLOR = Color.decode("#32CD32");
    private static final SimpleCommandExceptionType FAILED_EXCEPTION = new SimpleCommandExceptionType(
            () -> "Unknown command or insufficient permission"
    );

    public HelpCommand(@NotNull CommandCategory category, @NotNull String label) {
        super(category, label);
        final RequiredArgumentBuilder<BotCommandSource, String> commandArgument = argument("command", StringArgumentType.greedyString())
                .executes(this::executeUsage);

        this.argBuilder.then(commandArgument);
        this.argBuilder.executes(this::execute);
    }

    private int execute(CommandContext<BotCommandSource> ctx) {
        final BotCommandSource src = ctx.getSource();
        final JDiscordFab JDiscordFab = JDiscordFab.getInstance();
        final Map<String, DiscordFabCommand> map = JDiscordFab.getCommandManager().getCommands();
        map.remove(this.getLabel());

        EmbedBuilder builder = new EmbedBuilder().setTitle("Commands")
                .setDescription("Use `" + DISCORD_FAB.getConfig().prefix + "help <command>` for usage")
                .setColor(EMBED_COLOR);

        map.forEach((label, command) -> {
            if (command.getPredicate().test(src)) {
                builder.addField(label, command.getDescription() == null ? "" : command.getDescription(), true);
            }
        });

        src.getChannel().sendMessage(builder.build()).queue();
        return SUCCESS;
    }

    private int executeUsage(final CommandContext<BotCommandSource> ctx) throws CommandSyntaxException {
        final CommandDispatcher<BotCommandSource> dispatcher = DISCORD_FAB.getCommandManager().getDispatcher();
        final BotCommandSource src = ctx.getSource();
        final String input = StringArgumentType.getString(ctx, "command");
        final DiscordFabCommand command = DiscordFabCommand.getByLabel(input.split(" ")[0]);

        if (command == null || !command.getPredicate().test(src)) {
            throw FAILED_EXCEPTION.create();
        }

        final ParseResults<BotCommandSource> parseResults = dispatcher.parse(input.split(" ")[0], ctx.getSource());
        final EmbedBuilder builder = new EmbedBuilder().setTitle("Command: " + command.getLabel())
                .setColor(EMBED_COLOR)
                .setDescription(command.getDescription());

        if (command.getAlias() != null) {
            StringBuilder stringBuilder = new StringBuilder();
            for (int i = 0; i < command.getAlias().size(); i++) {
                stringBuilder.append(command.getAlias().get(i));

                if (i + 1 < command.getAlias().size()) {
                    stringBuilder.append(", ");
                }
            }

            builder.addField("Alias", stringBuilder.toString(), false);
        }


        Map<CommandNode<BotCommandSource>, String> map = dispatcher.getSmartUsage(Iterables.getLast(parseResults.getContext().getNodes()).getNode(), src);
        boolean first = true;
        for (Map.Entry<CommandNode<BotCommandSource>, String> entry : map.entrySet()) {
            builder.addField(
                    first ? "Usage" : "",
                    DISCORD_FAB.getConfig().prefix + parseResults.getReader().getString() + " " + entry.getValue(),
                    false
            );
            first = false;
        }

        src.sendFeedback(builder.build()).queue();
        return SUCCESS;
    }
}
