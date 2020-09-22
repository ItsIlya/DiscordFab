package io.github.hansi132.discordfab.discordbot.commands;

import io.github.hansi132.discordfab.discordbot.api.text.Messages;
import io.github.hansi132.discordfab.discordbot.util.DatabaseConnection;
import io.github.hansi132.discordfab.discordbot.util.LinkKeyCreator;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.ClickEvent;
import net.minecraft.text.MutableText;
import net.minecraft.util.Formatting;
import org.kilocraft.essentials.api.command.EssentialCommand;
import org.kilocraft.essentials.api.user.OnlineUser;
import org.kilocraft.essentials.util.messages.nodes.ExceptionMessageNode;
import org.kilocraft.essentials.util.text.Texter;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Random;

public class EssentialsDiscordLinkCommand extends EssentialCommand {
    public EssentialsDiscordLinkCommand(String label, String[] alias) {
        super(label, alias);
    }

    @Override
    public void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        this.argumentBuilder.executes(this::execute);
    }

    private int execute(final CommandContext<ServerCommandSource> ctx) throws CommandSyntaxException {
        OnlineUser user = this.getOnlineUser(ctx);
        Random random = new Random();
        int testKey = random.nextInt(10000);
        int linkKey;

        try {
            Connection connection = DatabaseConnection.connect();
            linkKey = LinkKeyCreator.checkKey(testKey);

            String selectSql = "SELECT McUUID, DiscordId, LinkKey FROM linkedaccounts WHERE McUUID = ?;";
            PreparedStatement selectStatement = connection.prepareStatement(selectSql);
            selectStatement.setString(1, user.getUuid().toString());
            ResultSet resultSet = selectStatement.executeQuery();

            if (resultSet.next()) {
                user.sendError(
                        getText(Texter.newText("You are already linked!&r Your link is: "),
                                resultSet.getInt("LinkKey"))
                );

                return FAILED;
            }

            user.sendMessage(getText(Texter.newText("Your link key is: "), linkKey));

            String insertSql = "INSERT INTO linkedaccounts (LinkKey, McUUID, McUsername) VALUES (?,?,?);";
            PreparedStatement insertStatement = connection.prepareStatement(insertSql);
            insertStatement.setInt(1, linkKey);
            insertStatement.setString(2, user.getUuid().toString());
            insertStatement.setString(3, user.getUsername());
            insertStatement.execute();

            connection.close();
        } catch (SQLException e) {
            user.sendError(
                    Texter.newText("Unexpected database error.").styled((style) ->
                            style.setHoverEvent(Texter.Events.onHover(Messages.getInnermostMessage(e))))
            );
        } catch (ClassNotFoundException e) {
            user.sendError(ExceptionMessageNode.UNKNOWN_COMMAND_EXCEPTION);
        }

        return SUCCESS;
    }

    private static MutableText getText(MutableText text, final int linkKey) {
        String key = String.format("%04d",linkKey);
        return Texter.newText().append(
                text.append(
                        Texter.newText(key).formatted(Formatting.AQUA).styled((style) ->
                                style.setHoverEvent(Texter.Events.onHover("Click to Copy"))
                                        .withClickEvent(
                                                new ClickEvent(
                                                        ClickEvent.Action.COPY_TO_CLIPBOARD,
                                                        key
                                                )
                                        )
                        )
                )
        );
    }
}
