package ch.mcserver.goliathPaperCore.common.database.mysql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class PlayerLocationRepository {

    private final MySQLManager mySQLManager;

    public PlayerLocationRepository(MySQLManager mySQLManager) {
        this.mySQLManager = mySQLManager;
    }

    public boolean exists(UUID uuid) {
        try (
                Connection connection = mySQLManager.getConnection();
                PreparedStatement statement = connection.prepareStatement(
                        "SELECT 1 FROM player_location WHERE uuid = ?"
                )
        ) {
            statement.setString(1, uuid.toString());

            try (ResultSet resultSet = statement.executeQuery()) {
                return resultSet.next();
            }

        } catch (SQLException exception) {
            exception.printStackTrace();
        }

        return false;
    }

    public PlayerLocationObject loadPlayer(UUID uuid) {
        try (
                Connection connection = mySQLManager.getConnection();
                PreparedStatement statement = connection.prepareStatement(
                        "SELECT * FROM player_location WHERE uuid = ?"
                )
        ) {
            statement.setString(1, uuid.toString());

            try (ResultSet resultSet = statement.executeQuery()) {

                if (!resultSet.next()) {
                    return null;
                }

                return new PlayerLocationObject(
                        UUID.fromString(resultSet.getString("uuid")),
                        resultSet.getTimestamp("created_at"),
                        resultSet.getFloat("yaw"),
                        resultSet.getFloat("pitch"),
                        resultSet.getInt("x"),
                        resultSet.getInt("y"),
                        resultSet.getInt("z"),
                        resultSet.getString("server")
                );
            }

        } catch (SQLException exception) {
            exception.printStackTrace();
        }

        return null;
    }

    public void save(PlayerLocationObject locationObject) {
        try (
                Connection connection = mySQLManager.getConnection();
                PreparedStatement statement = connection.prepareStatement(
                        """
                        INSERT INTO player_location
                        (uuid, created_at, yaw, pitch, x, y, z, server)
                        VALUES (?, ?, ?, ?, ?, ?, ?, ?)
                        ON DUPLICATE KEY UPDATE
                            created_at = VALUES(created_at),
                            yaw = VALUES(yaw),
                            pitch = VALUES(pitch),
                            x = VALUES(x),
                            y = VALUES(y),
                            z = VALUES(z),
                            server = VALUES(server)
                        """
                )
        ) {

            statement.setString(1, locationObject.getUuid().toString());
            statement.setTimestamp(2, locationObject.getTimestamp());
            statement.setFloat(3, locationObject.getYaw());
            statement.setFloat(4, locationObject.getPitch());
            statement.setInt(5, locationObject.getX());
            statement.setInt(6, locationObject.getY());
            statement.setInt(7, locationObject.getZ());
            statement.setString(8, locationObject.getServer());

            statement.executeUpdate();

        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }
}