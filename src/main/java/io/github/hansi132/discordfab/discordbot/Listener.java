package io.github.hansi132.discordfab.discordbot;

import io.github.hansi132.discordfab.JDiscordFab;
import io.github.hansi132.discordfab.discordbot.api.command.BotCommandSource;
import io.github.hansi132.discordfab.discordbot.integration.UserSynchronizer;
import io.github.hansi132.discordfab.discordbot.util.DatabaseConnection;
import net.dv8tion.jda.api.entities.ChannelType;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nonnull;
import java.sql.SQLException;
import java.util.Objects;

public class Listener extends ListenerAdapter {
    private static final Logger LOGGER = LogManager.getLogger();
    private static final JDiscordFab DISCORD_FAB = JDiscordFab.getInstance();

    @Override
    public void onReady(@Nonnull ReadyEvent event) {
        LOGGER.info("{} is ready", event.getJDA().getSelfUser().getAsTag());
        try {
            DatabaseConnection.connect();
            if (DISCORD_FAB.isDevelopment()) {
                LOGGER.info("First Database Connection successfully established");
            }
        } catch (ClassNotFoundException | SQLException e) {
            LOGGER.fatal("Could not connect to the Database!", e);
        }
    }

    @Override
    public void onMessageReceived(@Nonnull MessageReceivedEvent event) {
        final String raw = event.getMessage().getContentRaw();
        if (event.isFromType(ChannelType.PRIVATE) && !event.getAuthor().isBot()) {
            if (!UserSynchronizer.isLinkCode(raw)) {
                event.getPrivateChannel().sendMessage(DISCORD_FAB.getConfig().messages.invalid_link_key).queue();
                return;
            }

            UserSynchronizer.sync(event.getPrivateChannel(),null, event.getAuthor(), UserSynchronizer.getLinkCode(raw));
        }
    }

    @Override
    public void onGuildMessageReceived(@Nonnull GuildMessageReceivedEvent event) {
        final User user = event.getAuthor();
        if (user.isBot()) {
            return;
        }

        final String raw = event.getMessage().getContentDisplay();
        final String prefix = JDiscordFab.getInstance().getConfig().prefix;

        if (!event.isWebhookMessage() && !raw.equals(prefix) && raw.startsWith(prefix)) {
            final BotCommandSource src = new BotCommandSource(
                    event.getJDA(), event.getGuild(), event.getChannel(), user, event.getMember(), event
            );

            DISCORD_FAB.getCommandManager().execute(src, raw);
        } else if (!event.isWebhookMessage() && DISCORD_FAB.getConfig().chatSynchronizer.toMinecraft) {
            if (event.getChannel().getIdLong() == DISCORD_FAB.getConfig().chatSynchronizer.chatChannelId) {
                JDiscordFab.getInstance().getChatSynchronizer().onDiscordChat(Objects.requireNonNull(event.getMember()), raw, event.getMessage().getAttachments());
            }
        }
    }
}
