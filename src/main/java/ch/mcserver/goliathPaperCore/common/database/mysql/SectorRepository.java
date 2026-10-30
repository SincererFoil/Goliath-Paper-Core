package ch.mcserver.goliathPaperCore.common.database.mysql;

import ch.mcserver.goliathPaperCore.module.sector.SectorData;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class SectorRepository {

    private final MySQLManager mySQLManager;

    public SectorRepository(MySQLManager mySQLManager) {
        this.mySQLManager = mySQLManager;
    }

    private SectorData map(ResultSet resultSet) throws SQLException {
        return new SectorData(
                resultSet.getLong("id"),
                resultSet.getString("server_name"),
                resultSet.getString("dimension"),
                resultSet.getInt("min_chunk_x"),
                resultSet.getInt("max_chunk_x"),
                resultSet.getInt("min_chunk_z"),
                resultSet.getInt("max_chunk_z")
        );
    }

    public boolean exists(String serverName) throws SQLException {
        try (Connection connection = mySQLManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     "SELECT 1 FROM sectors WHERE server_name = ?"
             )) {

            statement.setString(1, serverName);

            try (ResultSet resultSet = statement.executeQuery()) {
                return resultSet.next();
            }
        }
    }

    public Optional<SectorData> findByServerName(String serverName) throws SQLException {

        try (Connection connection = mySQLManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     "SELECT * FROM sectors WHERE server_name = ?"
             )) {

            statement.setString(1, serverName);

            try (ResultSet resultSet = statement.executeQuery()) {
                return resultSet.next() ? Optional.of(map(resultSet)) : Optional.empty();
            }
        }
    }

    public Optional<SectorData> findById(long id) throws SQLException {
        try (Connection connection = mySQLManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     "SELECT * FROM sectors WHERE id = ?"
             )) {

            statement.setLong(1, id);

            try (ResultSet resultSet = statement.executeQuery()) {
                return resultSet.next() ? Optional.of(map(resultSet)) : Optional.empty();
            }
        }
    }

    public List<SectorData> findAll() throws SQLException {
        List<SectorData> sectors = new ArrayList<>();

        try (Connection connection = mySQLManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     "SELECT * FROM sectors ORDER BY id"
             );
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                sectors.add(map(resultSet));
            }
        }

        return sectors;
    }

    public List<SectorData> findByDimension(String dimension) throws SQLException {

        List<SectorData> sectors = new ArrayList<>();

        try (Connection connection = mySQLManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     "SELECT * FROM sectors WHERE dimension = ? ORDER BY id"
             )) {

            statement.setString(1, dimension);

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    sectors.add(map(resultSet));
                }
            }
        }

        return sectors;
    }

    public Optional<SectorData> findByChunk(String dimension, int chunkX, int chunkZ) throws SQLException {

        String sql = """
                SELECT * FROM sectors WHERE dimension = ? AND min_chunk_x <= ? AND max_chunk_x >= ? AND min_chunk_z <= ? AND max_chunk_z >= ? LIMIT 1
                """;

        try (Connection connection = mySQLManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, dimension);
            statement.setInt(2, chunkX);
            statement.setInt(3, chunkX);
            statement.setInt(4, chunkZ);
            statement.setInt(5, chunkZ);

            try (ResultSet resultSet = statement.executeQuery()) {
                return resultSet.next() ? Optional.of(map(resultSet)) : Optional.empty();
            }
        }
    }

    public boolean hasOverlap(String dimension, int minChunkX, int maxChunkX, int minChunkZ, int maxChunkZ, String excludeServerName) throws SQLException {

        try (Connection connection = mySQLManager.getConnection()) {
            return hasOverlap(connection, dimension, minChunkX, maxChunkX, minChunkZ, maxChunkZ, excludeServerName);
        }
    }

    private boolean hasOverlap(Connection connection, String dimension, int minChunkX, int maxChunkX, int minChunkZ, int maxChunkZ, String excludeServerName) throws SQLException {

        String sql = """
                SELECT 1 FROM sectors WHERE dimension = ? AND min_chunk_x <= ? AND max_chunk_x >= ? AND min_chunk_z <= ? AND max_chunk_z >= ? AND (? IS NULL OR server_name <> ?) LIMIT 1
                """;

        try (PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, dimension);
            statement.setInt(2, maxChunkX);
            statement.setInt(3, minChunkX);
            statement.setInt(4, maxChunkZ);
            statement.setInt(5, minChunkZ);
            statement.setString(6, excludeServerName);
            statement.setString(7, excludeServerName);

            try (ResultSet resultSet = statement.executeQuery()) {
                return resultSet.next();
            }
        }
    }

    public boolean insert(String serverName, String dimension, int minChunkX, int maxChunkX, int minChunkZ, int maxChunkZ) throws SQLException {

        String sql = """
                INSERT INTO sectors ( server_name, dimension, min_chunk_x, max_chunk_x, min_chunk_z, max_chunk_z
                ) VALUES (?, ?, ?, ?, ?, ?)
                """;

        try (Connection connection = mySQLManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            bindSector(statement, serverName, dimension, minChunkX, maxChunkX, minChunkZ, maxChunkZ);

            return statement.executeUpdate() == 1;
        }
    }

    public boolean update(String serverName, String dimension, int minChunkX, int maxChunkX, int minChunkZ, int maxChunkZ) throws SQLException {

        String sql = """
                UPDATE sectors SET dimension = ?, min_chunk_x = ?, max_chunk_x = ?, min_chunk_z = ?, max_chunk_z = ?
                WHERE server_name = ?
                """;

        try (Connection connection = mySQLManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, dimension);
            statement.setInt(2, minChunkX);
            statement.setInt(3, maxChunkX);
            statement.setInt(4, minChunkZ);
            statement.setInt(5, maxChunkZ);
            statement.setString(6, serverName);

            return statement.executeUpdate() > 0;
        }
    }

    public boolean delete(String serverName) throws SQLException {
        try (Connection connection = mySQLManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     "DELETE FROM sectors WHERE server_name = ?"
             )) {

            statement.setString(1, serverName);
            return statement.executeUpdate() > 0;
        }
    }

    public long count() throws SQLException {
        try (Connection connection = mySQLManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     "SELECT COUNT(*) FROM sectors"
             );
             ResultSet resultSet = statement.executeQuery()) {

            resultSet.next();
            return resultSet.getLong(1);
        }
    }

    public enum RegistrationResult {
        CREATED,
        ALREADY_REGISTERED,
        NAME_CONFLICT,
        RANGE_CONFLICT
    }

    public RegistrationResult register(String serverName, String dimension, int minChunkX, int maxChunkX, int minChunkZ, int maxChunkZ) throws SQLException {

        validateBounds(minChunkX, maxChunkX, minChunkZ, maxChunkZ);

        try (Connection connection = mySQLManager.getConnection()) {

            connection.setAutoCommit(false);

            boolean locked = false;

            try {
                locked = acquireRegistrationLock(connection);

                if (!locked) {
                    throw new SQLException("Could not acquire sector registry lock");
                }

                Optional<SectorData> existing = findByServerName(connection, serverName);

                if (existing.isPresent()) {
                    SectorData sector = existing.get();

                    boolean matches =
                            sector.dimension().equals(dimension) && sector.minChunkX() == minChunkX && sector.maxChunkX() == maxChunkX && sector.minChunkZ() == minChunkZ && sector.maxChunkZ() == maxChunkZ;

                    connection.commit();

                    return matches ? RegistrationResult.ALREADY_REGISTERED : RegistrationResult.NAME_CONFLICT;
                }

                if (hasOverlap(connection, dimension, minChunkX, maxChunkX, minChunkZ, maxChunkZ, null)) {
                    connection.commit();
                    return RegistrationResult.RANGE_CONFLICT;
                }

                String sql = """
                        INSERT INTO sectors (server_name, dimension, min_chunk_x, max_chunk_x, min_chunk_z, max_chunk_z) VALUES (?, ?, ?, ?, ?, ?)
                        """;

                try (PreparedStatement statement =
                             connection.prepareStatement(sql)) {

                    bindSector(statement, serverName, dimension, minChunkX, maxChunkX, minChunkZ, maxChunkZ
                    );

                    statement.executeUpdate();
                }

                connection.commit();
                return RegistrationResult.CREATED;

            } catch (SQLException | RuntimeException exception) {
                connection.rollback();
                throw exception;

            } finally {
                if (locked) {
                    releaseRegistrationLock(connection);
                }
            }
        }
    }

    private Optional<SectorData> findByServerName(Connection connection, String serverName) throws SQLException {

        try (PreparedStatement statement = connection.prepareStatement(
                "SELECT * FROM sectors WHERE server_name = ?"
        )) {

            statement.setString(1, serverName);

            try (ResultSet resultSet = statement.executeQuery()) {
                return resultSet.next() ? Optional.of(map(resultSet)) : Optional.empty();
            }
        }
    }

    private boolean acquireRegistrationLock(Connection connection)
            throws SQLException {

        try (PreparedStatement statement = connection.prepareStatement(
                "SELECT GET_LOCK('goliath_sector_registry', 10)"
        );
             ResultSet resultSet = statement.executeQuery()) {

            return resultSet.next() && resultSet.getInt(1) == 1;
        }
    }

    private void releaseRegistrationLock(Connection connection)
            throws SQLException {

        try (PreparedStatement statement = connection.prepareStatement(
                "SELECT RELEASE_LOCK('goliath_sector_registry')"
        )) {
            statement.executeQuery();
        }
    }

    private void validateBounds(int minChunkX, int maxChunkX, int minChunkZ, int maxChunkZ) {
        if (minChunkX > maxChunkX || minChunkZ > maxChunkZ) {
            throw new IllegalArgumentException("Invalid sector boundaries");
        }
    }

    private void bindSector(PreparedStatement statement, String serverName, String dimension, int minChunkX, int maxChunkX, int minChunkZ, int maxChunkZ) throws SQLException {

        statement.setString(1, serverName);
        statement.setString(2, dimension);
        statement.setInt(3, minChunkX);
        statement.setInt(4, maxChunkX);
        statement.setInt(5, minChunkZ);
        statement.setInt(6, maxChunkZ);
    }


}
