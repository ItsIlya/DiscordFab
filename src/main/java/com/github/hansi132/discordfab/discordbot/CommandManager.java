package com.github.hansi132.discordfab.discordbot;

import com.github.hansi132.discordfab.DiscordFab;
import com.github.hansi132.discordfab.discordbot.api.command.BotCommandSource;
import com.github.hansi132.discordfab.discordbot.api.command.CommandCategory;
import com.github.hansi132.discordfab.discordbot.api.command.DiscordFabCommand;
import com.github.hansi132.discordfab.discordbot.api.command.exception.BotCommandException;
import com.github.hansi132.discordfab.discordbot.api.text.Messages;
import com.github.hansi132.discordfab.discordbot.commands.*;
import com.github.hansi132.discordfab.discordbot.config.MainConfig;
import com.google.common.collect.Maps;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.ParseResults;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Locale;
import java.util.Map;

public class CommandManager {
    private static final MainConfig CONFIG = DiscordFab.getInstance().getConfig();
    private static final Logger LOGGER = LogManager.getLogger(DiscordFab.class);
    private final Map<String, DiscordFabCommand> commands;
    private final CommandDispatcher<BotCommandSource> dispatcher;

    public CommandManager() {
        this.commands = Maps.newHashMap();
        this.dispatcher = new CommandDispatcher<>();
//        CommandSyntaxException.BUILT_IN_EXCEPTIONS = new DiscordFormattedBuiltInExceptions();

        this.register(new DebugCommand());
        this.register(new PresenceCommand(CommandCategory.ADMIN, "presence", Permission.MANAGE_ROLES));
        this.register(new PingCommand(CommandCategory.GENERAL, "ping"));
        this.register(new IpCommand(CommandCategory.HELP, "ip", "connect"));
        this.register(new HelpCommand(CommandCategory.HELP, "help"));
        this.register(new OnlinePlayersCommand(CommandCategory.UTILITY, "online", "players"));
        this.register(new MinecraftAvatarCommand(CommandCategory.UTILITY, "minecraftavatar", "mcavatar", "skin"));
        this.register(new LinkCommand(CommandCategory.UTILITY, "link"));
//        this.register(new AvatarCommand(CommandCategory.UTILITY, "avatar"));
    }

    public <C extends DiscordFabCommand> void register(C command) {
        this.commands.put(command.getLabel().toLowerCase(Locale.ROOT), command.register(this.dispatcher));
    }

    @Nullable
    public DiscordFabCommand getCommand(final String label) {
        return this.commands.get(label.toLowerCase(Locale.ROOT));
    }

    public Map<String, DiscordFabCommand> getCommands() {
        return this.commands;
    }

    public void execute(@NotNull final BotCommandSource src, @NotNull final String input) {
        final String prefix = CONFIG.prefix;
        final StringReader reader = new StringReader(input);
        if (reader.canRead() && reader.getString().startsWith(prefix)) {
            reader.setCursor(prefix.length());
        }

        final String label = reader.getRemaining().split(" ")[0];
        final DiscordFabCommand command = DiscordFabCommand.getByLabel(label);
        if (command != null && !command.getPredicate().test(src)) {
            src.sendError(new EmbedBuilder().setDescription(CONFIG.messages.command_parse_no_permission)).queue();
            return;
        }

        //TODO: Cache Results
        final ParseResults<BotCommandSource> parseResults = this.dispatcher.parse(reader, src);

        try {
            try {
                this.dispatcher.execute(parseResults);
            } catch (BotCommandException e) {
                src.sendFeedback(e.getJDAMessage()).queue();
            } catch (CommandSyntaxException e) {
                if (command != null) {
                    EmbedBuilder builder = new EmbedBuilder().setDescription(e.getMessage());
                    builder.setFooter(CONFIG.messages.command_parse_help
                            .replace("$command$", prefix + "help " + label)
                    );
                    src.sendError(builder).queue();
                }
            }
        } catch (Exception e) {
            final EmbedBuilder builder = new EmbedBuilder()
                    .setTitle("An unexpected error occurred while trying to execute that command")
                    .addField("Exception message", Messages.getInnermostMessage(e), false);

            if (DiscordFab.getInstance().isDevelopment()) {
                StringBuilder stringBuilder = new StringBuilder();
                StackTraceElement[] elements = e.getStackTrace();
                for (int i = 0; i < Math.min(elements.length, 3); i++) {
                    final StackTraceElement element = elements[i];
                    stringBuilder.append("\n")
                            .append(element.getMethodName()).append("\n ").append(element.getFileName()).append(":")
                            .append(element.getLineNumber());
                }

                builder.addField("Stack trace", stringBuilder.toString(), false);
                LOGGER.error("Command exception {}", input, e);
            } else {
                LOGGER.error("'{}' threw an exception", input, e);
            }

            src.sendError(builder).queue();
        }

    }

    public CommandDispatcher<BotCommandSource> getDispatcher() {
        return this.dispatcher;
    }
}
