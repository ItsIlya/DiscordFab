package io.github.hansi132.discordfab.discordbot.api.command;

import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.requests.restaction.MessageAction;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.CompletableFuture;

public interface IDiscordCommandSource {
    List<Member> getAllGuildMembers();

    JDA getJDA();

    String getName();

    String getDisplayName();

    Guild getGuild();

    User getUser();

    @Nullable
    Member getMember();

    boolean isAuthorized(@NotNull final Permission permission);

    boolean isAuthorized(@NotNull final Collection<Permission> permissions);

    boolean isAuthorized(@NotNull final GuildChannel channel, @NotNull final Permission... permissions);

    boolean isAuthorized(@NotNull final GuildChannel channel, @NotNull final Collection<Permission> permissions);

    TextChannel getChannel();

    MessageAction sendFeedback(@NotNull final CharSequence sequence);

    MessageAction sendFeedback(@NotNull final String string, @Nullable Object... objects);

    MessageAction sendFeedback(@NotNull final Message message);

    MessageAction sendFeedback(@NotNull final MessageEmbed embed);

    MessageAction sendError(@NotNull final EmbedBuilder builder);

    MessageAction sendWarning(@NotNull final EmbedBuilder builder);

    GuildMessageReceivedEvent getEvent();

    static CompletableFuture<Suggestions> suggestMatching(Iterable<String> iterable, SuggestionsBuilder builder) {
        String string = builder.getRemaining().toLowerCase(Locale.ROOT);

        for (String s : iterable) {
            if (matches(string, s.toLowerCase(Locale.ROOT))) {
                builder.suggest(s);
            }
        }

        return builder.buildFuture();
    }

    static boolean matches(String string_1, String string_2) {
        for (int i = 0; !string_2.startsWith(string_1, i); i++) {
            i = string_2.indexOf(95, i);
            if (i > 0) {
                return false;
            }
        }

        return true;
    }

}
