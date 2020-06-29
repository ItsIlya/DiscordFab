package com.github.hansi132.discordfab.discordbot.integration;

import com.github.hansi132.discordfab.DiscordFab;
import com.github.hansi132.discordfab.discordbot.config.DataConfig;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.User;

import java.sql.*;
import java.util.List;

public class AssignNick {
    public AssignNick(int linkKey) throws SQLException, ClassNotFoundException {
        //Database
        String db = new DataConfig().getProperty("database");
        String dbUser = new DataConfig().getProperty("databaseUser");
        String dbPassword = new DataConfig().getProperty("databasePassword");
        Class.forName("com.mysql.jdbc.Driver");
        Connection connection = DriverManager.getConnection(db, dbUser, dbPassword);

        String selectSql = "SELECT DiscordId, McUsername FROM linkedaccounts WHERE LinkKey = ?;";
        PreparedStatement selectStmt = connection.prepareStatement(selectSql);
        selectStmt.setInt(1, linkKey);
        ResultSet resultSet = selectStmt.executeQuery();
        resultSet.next();
        String discordId = resultSet.getString("DiscordId");
        String mcUsername = resultSet.getString("McUsername");

        Guild guild = DiscordFab.getBot().getGuildById(new DataConfig().getProperty("guild"));
        User user = DiscordFab.getBot().getUserById(discordId);
        List<Role> role = guild.getRolesByName(new DataConfig().getProperty("role"), true);
        guild.getMember(user).modifyNickname(mcUsername).complete();
        guild.addRoleToMember(guild.getMember(user), role.get(0)).complete();
    }
}
