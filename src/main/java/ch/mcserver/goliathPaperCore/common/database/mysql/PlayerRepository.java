package ch.mcserver.goliathPaperCore.common.database.mysql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class PlayerRepository {

    private final ch.mcserver.goliathPaperCore.common.database.mysql.MySQLManager mySQLManager;
    private static final ZoneId ZONE = ZoneId.of("Europe/Zurich");

    public PlayerRepository(MySQLManager mySQLManager) {
        this.mySQLManager = mySQLManager;
    }

    public boolean exists(UUID uuid) {
        try (Connection connection = mySQLManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     "SELECT * FROM players WHERE uuid = ?"
             )) {

            statement.setString(1, uuid.toString());

            try (ResultSet resultSet = statement.executeQuery()) {
                return resultSet.next();
            }

        } catch (SQLException exception) {
            exception.printStackTrace();
        }

        return false;
    }

    public boolean existsByUsername(String name) {
        try (Connection connection = mySQLManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     "SELECT * FROM players WHERE name = ?"
             )) {

            statement.setString(1, name);

            try (ResultSet resultSet = statement.executeQuery()) {
                return resultSet.next();
            }

        } catch (SQLException exception) {
            exception.printStackTrace();
        }

        return false;
    }

    public PlayerObject loadPlayer(UUID uuid) {
        try (Connection connection = mySQLManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     "SELECT * FROM players WHERE uuid = ?"
             )) {

            statement.setString(1, uuid.toString());

            try (ResultSet resultSet = statement.executeQuery()) {

                if (!resultSet.next()) {
                    return null;
                }

                UUID playerUuid = UUID.fromString(resultSet.getString("uuid"));

                return new PlayerObject(
                        playerUuid,
                        resultSet.getString("name"),
                        resultSet.getString("prefix"),
                        resultSet.getString("current_server"),
                        resultSet.getBoolean("sfmode"),
                        resultSet.getBoolean("debug_mode"),
                        resultSet.getBoolean("gmsp"),
                        resultSet.getBoolean("vanished"),
                        resultSet.getFloat("fly_speed"),
                        resultSet.getLong("first_join"),
                        resultSet.getLong("last_join")
                );
            }

        } catch (SQLException exception) {
            exception.printStackTrace();
        }

        return null;
    }

    public PlayerObject loadPlayerByUsername(String username) {
        try (Connection connection = mySQLManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     "SELECT * FROM players WHERE name = ?"
             )) {

            statement.setString(1, username);

            try (ResultSet resultSet = statement.executeQuery()) {

                if (!resultSet.next()) {
                    return null;
                }

                UUID playerUuid = UUID.fromString(resultSet.getString("uuid"));

                return new PlayerObject(
                        playerUuid,
                        resultSet.getString("name"),
                        resultSet.getString("prefix"),
                        resultSet.getString("current_server"),
                        resultSet.getBoolean("sfmode"),
                        resultSet.getBoolean("debug_mode"),
                        resultSet.getBoolean("gmsp"),
                        resultSet.getBoolean("vanished"),
                        resultSet.getFloat("fly_speed"),
                        resultSet.getLong("first_join"),
                        resultSet.getLong("last_join")
                );
            }

        } catch (SQLException exception) {
            exception.printStackTrace();
        }

        return null;
    }


    public void save(PlayerObject playerObject) {
        try (Connection connection = mySQLManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     """
                     UPDATE players
                     SET name = ?, prefix = ?, sfmode = ?, debug_mode = ?, gmsp = ?, vanished = ?,
                         fly_speed = ?, current_server = ?, last_join = ?
                     WHERE uuid = ?
                     """
             )) {

            statement.setString(1, playerObject.getName());
            statement.setString(2, playerObject.getPrefix());
            statement.setBoolean(3, playerObject.isSfmode());
            statement.setBoolean(4, playerObject.isDebugMode());
            statement.setBoolean(5, playerObject.isGmsp());
            statement.setBoolean(6, playerObject.isVanished());
            statement.setFloat(7, playerObject.getFlySpeed());
            statement.setString(8, playerObject.getCurrentServer());
            statement.setLong(9, playerObject.getLastJoin());
            statement.setString(10, playerObject.getUuid().toString());

            statement.executeUpdate();

        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }


    public void savePlayerDataOnly(PlayerObject playerObject) {
        try (Connection connection = mySQLManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     """
                     UPDATE players
                     SET name = ?, prefix = ?, sfmode = ?, debug_mode = ?, gmsp = ?, vanished = ?,
                         fly_speed = ?, current_server = ?, last_join = ?
                     WHERE uuid = ?
                     """
             )) {

            statement.setString(1, playerObject.getName());
            statement.setString(2, playerObject.getPrefix());
            statement.setBoolean(3, playerObject.isSfmode());
            statement.setBoolean(4, playerObject.isDebugMode());
            statement.setBoolean(5, playerObject.isGmsp());
            statement.setBoolean(6, playerObject.isVanished());
            statement.setFloat(7, playerObject.getFlySpeed());
            statement.setString(8, playerObject.getCurrentServer());
            statement.setLong(9, playerObject.getLastJoin());
            statement.setString(10, playerObject.getUuid().toString());

            statement.executeUpdate();

        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }

    public List<String> getAllUsernames() {
        List<String> usernames = new ArrayList<>();
        try (Connection connection = mySQLManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     "SELECT name FROM players"
             );
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                usernames.add(resultSet.getString("name"));
            }

        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        return usernames;
    }

    public List<String> getAllPunishedUsernames() {
        List<String> usernames = new ArrayList<>();
        try (Connection connection = mySQLManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     "SELECT DISTINCT players.name FROM players " +
                             "INNER JOIN player_punishments ON players.uuid = player_punishments.player_uuid"
             );
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                usernames.add(resultSet.getString("name"));
            }

        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        return usernames;
    }
}