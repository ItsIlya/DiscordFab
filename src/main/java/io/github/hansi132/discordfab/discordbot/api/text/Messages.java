package io.github.hansi132.discordfab.discordbot.api.text;

import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.entities.Message;
import org.jetbrains.annotations.NotNull;

public class Messages {
    public static Message exceptionToMessage(@NotNull final Exception e, final int maxStackTraceElements) {
        MessageBuilder message = new MessageBuilder(e.getMessage() == null ? e.getClass().getName() : e.getMessage());
        StackTraceElement[] elements = e.getStackTrace();
        for (int i = 0; i < Math.min(elements.length, maxStackTraceElements); i++) {
            final StackTraceElement element = elements[i];
            message.append("\n")
                    .append(element.getMethodName())
                    .append("\n ")
                    .append(element.getFileName())
                    .append(":")
                    .append(String.valueOf(element.getLineNumber()));
        }

        return message.build();
    }

    public static String getInnermostMessage(@NotNull final Throwable throwable) {
        return throwable.getCause() == null ?
                throwable.getMessage() == null ?
                        throwable.toString() : throwable.getMessage() :
                getInnermostMessage(throwable.getCause());
    }
}
