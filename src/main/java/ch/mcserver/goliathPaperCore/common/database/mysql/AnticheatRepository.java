package ch.mcserver.goliathPaperCore.common.database.mysql;

import ch.mcserver.goliathPaperCore.module.anticheat.data.CheckState;
import ch.mcserver.goliathPaperCore.module.anticheat.data.PlayerData;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class AnticheatRepository {

    private final MySQLManager mySQLManager;

    public AnticheatRepository(MySQLManager mySQLManager) {
        this.mySQLManager = mySQLManager;
    }

    public void saveCheckStates(List<CheckState> states) {
        try (
                Connection connection = mySQLManager.getConnection();
                PreparedStatement statement = connection.prepareStatement(
                        """
                        INSERT INTO anticheat_check_state
                        (player_uuid, check_id, violations, buffer)
                        VALUES (?, ?, ?, ?) 
                        ON DUPLICATE KEY UPDATE
                                         violations = VALUES(violations),
                                         buffer = VALUES(buffer)
                                         
                        """
                )
        ) {
            for (CheckState state : states) {
                statement.setString(1, state.playerUuid().toString());
                statement.setString(2, state.checkId());
                statement.setInt(3, state.violations());
                statement.setDouble(4, state.buffer());
                statement.addBatch();
            }

            statement.executeBatch();

        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }

    public void saveCheckState(CheckState checkState) {
        try (
                Connection connection = mySQLManager.getConnection();
                PreparedStatement statement = connection.prepareStatement(
                        """
                        INSERT INTO anticheat_check_state
                        (player_uuid, check_id, violations, buffer)
                        VALUES (?, ?, ?, ?) 
                        ON DUPLICATE KEY UPDATE
                                         violations = VALUES(violations),
                                         buffer = VALUES(buffer)
                                         
                        """
                )
        ) {
            statement.setString(1, checkState.playerUuid().toString());
            statement.setString(2, checkState.checkId());
            statement.setInt(3, checkState.violations());
            statement.setDouble(4, checkState.buffer());

            statement.executeUpdate();

        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }

    public List<CheckState> loadCheckStates(UUID uuid) {
        List<CheckState> states = new ArrayList<>();

        try (
                Connection connection = mySQLManager.getConnection();
                PreparedStatement statement = connection.prepareStatement(
                        """
                        SELECT player_uuid, check_id, violations, buffer
                        FROM anticheat_check_state
                        WHERE player_uuid = ?
                        """
                )
        ) {
            statement.setString(1, uuid.toString());

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    states.add(new CheckState(
                            UUID.fromString(resultSet.getString("player_uuid")),
                            resultSet.getString("check_id"),
                            resultSet.getInt("violations"),
                            resultSet.getDouble("buffer")
                    ));
                }
            }
        } catch (SQLException exception) {
            exception.printStackTrace();
        }

        return states;
    }
}
