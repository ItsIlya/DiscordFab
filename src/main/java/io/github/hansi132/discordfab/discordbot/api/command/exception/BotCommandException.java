package io.github.hansi132.discordfab.discordbot.api.command.exception;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.dv8tion.jda.api.entities.Message;

public class BotCommandException extends RuntimeException {
    private final Message message;

    public BotCommandException(final Message message) {
        super(message.getContentRaw(), null, CommandSyntaxException.ENABLE_COMMAND_STACK_TRACES, CommandSyntaxException.ENABLE_COMMAND_STACK_TRACES);
        this.message = message;
    }

    public Message getJDAMessage() {
        return this.message;
    }
}
