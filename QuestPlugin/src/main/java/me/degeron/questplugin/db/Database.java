package me.degeron.questplugin.db;

import me.degeron.questplugin.Main;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import java.sql.*;

public class Database {

    private Connection connection;

    private ConfigurationSection dbSection = Main.getInstance().getConfig().getConfigurationSection("database");

    public Connection getConnection() throws SQLException{
        if (connection != null) {
            return connection;
        }
        String url = "jdbc:mysql://" +
                dbSection.getString("host") + ":" +
                dbSection.getString("port") + "/" +
                dbSection.getString("data_base_name");

        String user = dbSection.getString("user");
        String password = dbSection.getString("password");

        this.connection = DriverManager.getConnection(url, user, password);
        System.out.println("CONNECT TO DATABASE");
        return this.connection;
    }


    public void initializeDatabase() throws SQLException{
        Statement statement = getConnection().createStatement();
        statement.execute("CREATE TABLE IF NOT EXISTS player_stats(uuid varchar(36) primary key, completed int, canceled int, selected_quest text, progress_in_quest int)");
        statement.close();
        System.out.println("Create the stats table in the database!");
    }

    // Здесь базовый CRUD для бд, думаю его не стоит описывать
    public PlayerStats findPlayerStatsByUUID(String uuid) throws SQLException{
        PreparedStatement statement = getConnection().prepareStatement("SELECT * FROM player_stats WHERE uuid = ?");
        statement.setString(1, uuid);
        ResultSet results = statement.executeQuery();
        if (results.next()) {
            int completed = results.getInt("completed");
            int canceled = results.getInt("canceled");
            String selectedQuest = results.getString("selected_quest");
            int progressInQuest = results.getInt("progress_in_quest");

            PlayerStats playerStats = new PlayerStats(uuid, completed, canceled, selectedQuest, progressInQuest);
            statement.close();
            return playerStats;
        }
        statement.close();
        return null;
    }

    public void createPlayerStats(PlayerStats stats) throws SQLException {
        PreparedStatement statement = getConnection()
                .prepareStatement("INSERT INTO player_stats(uuid, completed, canceled, selected_quest, progress_in_quest) VALUES (?, ?, ?, ?, ?)");

        statement.setString(1, stats.getUUID());
        statement.setInt(2, stats.getCompleted());
        statement.setInt(3, stats.getCanceled());
        statement.setString(4, stats.getSelectedQuest());
        statement.setInt(5, stats.getProgressInQuest());

        statement.executeUpdate();
        statement.close();
    }

    public void updatePlayerStats(PlayerStats stats) throws SQLException {
        PreparedStatement statement = getConnection()
                .prepareStatement("UPDATE player_stats SET completed = ?, canceled = ?, selected_quest = ?, progress_in_quest = ? WHERE uuid = ?");

        statement.setInt(1, stats.getCompleted());
        statement.setInt(2, stats.getCanceled());
        statement.setString(3, stats.getSelectedQuest());
        statement.setInt(4, stats.getProgressInQuest());
        statement.setString(5, stats.getUUID());

        statement.executeUpdate();
        statement.close();
    }

    public void deletePlayerStats(String uuid) throws SQLException {
        PreparedStatement statement = getConnection()
                .prepareStatement("DELETE FROM player_stats WHERE uuid = ?");
        statement.setString(1, uuid);
        statement.executeUpdate();
        statement.close();
    }

    // Получить игрока, если его нет в бд, то добавить
    public PlayerStats getPlayerStatsFromDatabase(Player player) throws SQLException {
        PlayerStats stats = findPlayerStatsByUUID(player.getUniqueId().toString());

        if (stats == null) {
            stats = new PlayerStats(player.getUniqueId().toString(), 0, 0, "", 0);
            createPlayerStats(stats);
            return stats;
        }
        return stats;
    }

    // Стираем выполненные и отменённые квесты из поля выбранного квеста
    public void replaceDoneOrCancelSelectedQuests() throws SQLException {
        Statement statement = connection.createStatement();
        ResultSet results = statement.executeQuery("SELECT uuid FROM player_stats");

        while (results.next()) {
            PlayerStats stats = findPlayerStatsByUUID(results.getString("uuid"));
            if (stats.getSelectedQuest().equals("done") || stats.getSelectedQuest().equals("cancel")) {
                stats.setSelectedQuest("");
            }
            updatePlayerStats(stats);
        }


        statement.close();
    }

    // Чтобы подлючение не терялось
    public void againstTimeouts() throws SQLException {
        Statement statement = connection.createStatement();
        statement.executeQuery("SELECT uuid FROM player_stats");
        statement.close();
    }


    public void closeConnection() {
        try {
            if (this.connection != null) {
                this.connection.close();
            }
        } catch (SQLException error) {
            error.printStackTrace();
        }
    }
}
