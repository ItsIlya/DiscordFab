package com.github.hansi132.discordfab.discordbot.api.command;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.requests.restaction.MessageAction;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.awt.*;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

public class BotCommandSource implements IDiscordCommandSource {
    private static final String NO_MEMBER_WEBHOOK_MESSAGE = "You can't get the member for a Webhook Message!";
    private static final Color ERROR_COLOR = Color.decode("#FF0033");
    private static final Color WARNING_COLOR = Color.decode("#FFCC00");
    private final JDA api;
    private final Guild guild;
    private final TextChannel channel;
    private final User user;
    private final Member member;
    private final GuildMessageReceivedEvent event;

    public BotCommandSource(@NotNull final JDA api,
                            @NotNull final Guild guild,
                            @NotNull final TextChannel channel,
                            @NotNull final User user,
                            @Nullable final Member member,
                            @NotNull final GuildMessageReceivedEvent event) {
        this.api = api;
        this.guild = guild;
        this.channel = channel;
        this.user = user;
        this.member = member;
        this.event = event;
    }

    @Override
    public List<Member> getAllGuildMembers() {
        return this.guild.getMembers();
    }

    @Override
    public JDA getJDA() {
        return this.api;
    }

    @Override
    public String getName() {
        return this.user.getName();
    }

    @Override
    public String getDisplayName() {
        return this.member == null ? this.getName() : this.member.getNickname();
    }

    @Override
    public Guild getGuild() {
        return this.guild;
    }

    @Override
    public User getUser() {
        return this.user;
    }

    @Nullable
    @Override
    public Member getMember() {
        return this.member;
    }

    @Override
    public boolean isAuthorized(@NotNull Permission permission) {
        return Objects.requireNonNull(this.member, NO_MEMBER_WEBHOOK_MESSAGE).hasPermission(permission);
    }

    @Override
    public boolean isAuthorized(@NotNull Collection<Permission> permissions) {
        return Objects.requireNonNull(this.member, NO_MEMBER_WEBHOOK_MESSAGE).hasPermission(permissions);
    }

    @Override
    public boolean isAuthorized(@NotNull GuildChannel channel, @NotNull Permission... permissions) {
        return Objects.requireNonNull(this.member, NO_MEMBER_WEBHOOK_MESSAGE).hasPermission(channel, permissions);
    }

    @Override
    public boolean isAuthorized(@NotNull GuildChannel channel, @NotNull Collection<Permission> permissions) {
        return Objects.requireNonNull(this.member, NO_MEMBER_WEBHOOK_MESSAGE).hasPermission(channel, permissions);
    }

    @Override
    public TextChannel getChannel() {
        return this.channel;
    }

    @Override
    public MessageAction sendFeedback(@NotNull CharSequence charSequence) {
        return this.channel.sendMessage(charSequence);
    }

    @Override
    public MessageAction sendFeedback(@NotNull String string, @NotNull Object... objects) {
        return this.channel.sendMessageFormat(string, objects);
    }

    @Override
    public MessageAction sendFeedback(@NotNull Message message) {
        return this.channel.sendMessage(message);
    }

    @Override
    public MessageAction sendFeedback(@NotNull MessageEmbed embed) {
        return this.channel.sendMessage(embed);
    }

    @Override
    public MessageAction sendError(@NotNull EmbedBuilder builder) {
        return this.sendFeedback(builder.setColor(ERROR_COLOR).build());
    }

    @Override
    public MessageAction sendError(@NotNull CharSequence charSequence) {
        return this.sendError(new EmbedBuilder().setDescription(charSequence));
    }

    @Override
    public MessageAction sendWarning(@NotNull EmbedBuilder builder) {
        return this.sendFeedback(builder.setColor(WARNING_COLOR).build());
    }

    @Override
    public GuildMessageReceivedEvent getEvent() {
        return this.event;
    }

}
