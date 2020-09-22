package io.github.hansi132.discordfab.discordbot.commands;

import io.github.hansi132.discordfab.discordbot.api.command.BotCommandSource;
import io.github.hansi132.discordfab.discordbot.api.command.CommandCategory;
import io.github.hansi132.discordfab.discordbot.api.command.DiscordFabCommand;
import io.github.hansi132.discordfab.discordbot.util.EmbedUtil;
import com.mojang.brigadier.context.CommandContext;
import net.dv8tion.jda.api.EmbedBuilder;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.kilocraft.essentials.api.KiloEssentials;

public class IpCommand extends DiscordFabCommand {

    public IpCommand(@NotNull CommandCategory category, @NotNull String label, @Nullable String... alias) {
        super(category, label, alias);
        this.withDescription("Gives the server ip and version");
        this.argBuilder.executes(this::execute);
    }

    private int execute(CommandContext<BotCommandSource> ctx) {
        BotCommandSource src = ctx.getSource();
        EmbedUtil embedUtil = DISCORD_FAB.getEmbedUtil();
        EmbedBuilder builder = new EmbedBuilder()
                .setAuthor(
                        KiloEssentials.getServer().getName(),
                        embedUtil.getAuthorLink(),
                        embedUtil.getAuthorIconURL().isEmpty() ? null : embedUtil.getAuthorIconURL()
                )
                .addField("IP", String.valueOf(DISCORD_FAB.getConfig().serverIp), false)
                .addField("Version", KiloEssentials.getServer().getMinecraftServer().getVersion(), false)
                .setColor(embedUtil.getDefaultColor());

        src.sendFeedback(builder.build()).queue();
        return SUCCESS;
    }
}
