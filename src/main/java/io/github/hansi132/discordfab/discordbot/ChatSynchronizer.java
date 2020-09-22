package io.github.hansi132.discordfab.discordbot;

import club.minnced.discord.webhook.send.WebhookMessageBuilder;
import io.github.hansi132.discordfab.JDiscordFab;
import io.github.hansi132.discordfab.discordbot.config.section.chatsync.ChatSynchronizerConfigSection;
import io.github.hansi132.discordfab.discordbot.integration.UserSynchronizer;
import io.github.hansi132.discordfab.discordbot.user.DiscordBroadcaster;
import io.github.hansi132.discordfab.discordbot.util.MinecraftAvatar;
import com.google.common.collect.Maps;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.minecraft.text.LiteralText;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.kilocraft.essentials.api.text.TextFormat;
import org.kilocraft.essentials.api.user.User;
import org.kilocraft.essentials.chat.KiloChat;
import org.kilocraft.essentials.chat.TextMessage;
import org.kilocraft.essentials.util.RegexLib;
import org.kilocraft.essentials.util.text.Texter;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.regex.Pattern;

public class ChatSynchronizer {
    private static final JDiscordFab DISCORD_FAB = JDiscordFab.getInstance();
    private static final ChatSynchronizerConfigSection CONFIG = DISCORD_FAB.getConfig().chatSynchronizer;
    private static final Pattern LINK_PATTERN = Pattern.compile(RegexLib.URL.get());
    private static final int LINK_MAX_LENGTH = 20;
    private final DiscordBroadcaster discordBroadcaster;
    private final Map<UUID, net.dv8tion.jda.api.entities.User> map = Maps.newHashMap();


    public ChatSynchronizer() {
        this.discordBroadcaster = new DiscordBroadcaster();
    }

    private static String getMCAvatarURL(@NotNull final UUID uuid) {
        MinecraftAvatar.RenderType renderType = MinecraftAvatar.RenderType.getByName(CONFIG.renderOptions.renderType);
        if (renderType == null) {
            renderType = MinecraftAvatar.RenderType.AVATAR;
        }

        return MinecraftAvatar.generateUrl(uuid,
                renderType,
                MinecraftAvatar.RenderType.Model.DEFAULT,
                CONFIG.renderOptions.size,
                CONFIG.renderOptions.scale,
                CONFIG.renderOptions.showOverlay
        );
    }

    public void onGameChat(@NotNull final User user, @NotNull final String string) {
        WebhookMessageBuilder builder = new WebhookMessageBuilder();
        setMetaFor(user, builder);

        String content = TextFormat.clearColorCodes(string.replaceAll("@", ""));
        builder.setContent(content);

        this.discordBroadcaster.send(builder.build());
    }

    public void sendtoGame(final Member member, @NotNull final Text content) {
        MutableText text = new TextMessage(CONFIG.messages.prefix
                .replace("%name%", member.getEffectiveName())).toText()
                .append(content);
        KiloChat.broadCast(text);
    }

    public void onDiscordChat(final Member member, @NotNull final String string, List<Message.Attachment> attachments) {
        for (Message.Attachment attachment : attachments) {
            MutableText text;
            if (attachment.isImage()) {
                text = new LiteralText("[IMAGE]");
            } else if (attachment.isVideo()) {
                text = new LiteralText("[VIDEO]");
            } else {
                text = new LiteralText("[FILE]");
            }
            MutableText hover = new LiteralText("")
                    .append(new LiteralText("Name: ").formatted(Formatting.GRAY))
                    .append(new LiteralText(attachment.getFileName()).formatted(Formatting.AQUA))
                    .append(new LiteralText("\nResolution: ").formatted(Formatting.GRAY))
                    .append(new LiteralText(attachment.getWidth() + "x" + attachment.getHeight()).formatted(Formatting.AQUA))
                    .append(new LiteralText("\nSize: ").formatted(Formatting.GRAY))
                    .append(new LiteralText(attachment.getSize() / 1024 + "kb").formatted(Formatting.AQUA));
            text.styled(style -> style.withHoverEvent(Texter.Events.onHover(hover)).withClickEvent(Texter.Events.onClickOpen(attachment.getUrl()))).formatted(Formatting.GREEN);
            sendtoGame(member, text);
        }
        if (!string.equals("")) {
            sendtoGame(member, new TextMessage(string).toText());
        }
    }

    @Nullable
    private net.dv8tion.jda.api.entities.User getJDAUser(@NotNull final UUID uuid) {
        if (this.map.containsKey(Objects.requireNonNull(uuid))) {
            return this.map.get(uuid);
        }

        long discordId = UserSynchronizer.getSyncedUserId(uuid);
        if (discordId == 0L) {
            return null;
        }

        net.dv8tion.jda.api.entities.User user = JDiscordFab.getBot().getUserById(discordId);

        if (user == null) {
            return null;
        }

        this.map.put(uuid, user);
        return user;
    }

    public void onUserJoin(@NotNull final User user) {
        WebhookMessageBuilder builder = new WebhookMessageBuilder();
        setMetaFor(user, builder);
        builder.setContent(CONFIG.messages.userJoin.replace("%name%", user.getName()));

        this.discordBroadcaster.send(builder.build());
    }

    public void onUserLeave(@NotNull final User user) {
        WebhookMessageBuilder builder = new WebhookMessageBuilder();
        setMetaFor(user, builder);
        builder.setContent(CONFIG.messages.userLeave.replace("%name%", user.getName()));

        this.discordBroadcaster.send(builder.build());
        this.map.remove(user.getId());
    }

    public void setMetaFor(@NotNull final User user, @NotNull final WebhookMessageBuilder builder) {
        if (UserSynchronizer.isLinked(user.getUuid())) {
            net.dv8tion.jda.api.entities.User discordUser = this.getJDAUser(user.getUuid());
            if (discordUser != null && discordUser.getAvatarUrl() != null) {
                builder.setAvatarUrl(discordUser.getAvatarUrl());
                builder.setUsername(discordUser.getName());
            }
        } else {
            builder.setAvatarUrl(getMCAvatarURL(user.getUuid()));
            builder.setUsername(user.getUsername());
        }
    }

    public void shutdown() {
        this.discordBroadcaster.getClient().close();
    }
}
