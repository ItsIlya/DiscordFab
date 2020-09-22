package io.github.hansi132.discordfab.discordbot.commands;

import io.github.hansi132.discordfab.discordbot.api.command.BotCommandSource;
import io.github.hansi132.discordfab.discordbot.api.command.CommandCategory;
import io.github.hansi132.discordfab.discordbot.api.command.DiscordFabCommand;
import com.mojang.brigadier.context.CommandContext;
import net.dv8tion.jda.api.EmbedBuilder;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.kilocraft.essentials.api.KiloEssentials;
import org.kilocraft.essentials.api.user.OnlineUser;

import java.util.List;


public class OnlinePlayersCommand extends DiscordFabCommand {

    public OnlinePlayersCommand(@NotNull CommandCategory category, @NotNull String label, @Nullable String... alias) {
        super(category, label, alias);
        this.withDescription("Gives you the current online players.");
        this.argBuilder.executes(this::execute);
    }

    private int execute(CommandContext<BotCommandSource> ctx) {
        BotCommandSource src = ctx.getSource();
        List<OnlineUser> users = KiloEssentials.getServer().getUserManager().getOnlineUsersAsList();

        EmbedBuilder builder = new EmbedBuilder()
                .setColor(DISCORD_FAB.getEmbedUtil().getDefaultColor())
                .setTitle("Online Players");

        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < users.size(); i++) {
            stringBuilder.append(users.get(i).getName());

            if (i + 1 < users.size()) {
                stringBuilder.append(", ");
            }
        }

        builder.addField(users.size() + " online", stringBuilder.toString(), false);
        src.sendFeedback(builder.build()).queue();

        return SUCCESS;
    }
}

