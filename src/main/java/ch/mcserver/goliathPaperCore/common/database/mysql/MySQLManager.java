package ch.mcserver.goliathPaperCore.common.database.mysql;

import ch.mcserver.goliathPaperCore.GoliathPaperCore;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.sql.Connection;
import java.sql.SQLException;

public class MySQLManager {

    private HikariDataSource dataSource;

    public void connect() {
        String host = GoliathPaperCore.config.node("mysql", "host").getString("127.0.0.1");
        int port = GoliathPaperCore.config.node("mysql", "port").getInt(3306);
        String database = GoliathPaperCore.config.node("mysql", "database").getString("goliath");
        String username = GoliathPaperCore.config.node("mysql", "username").getString("goliath");
        String password = GoliathPaperCore.config.node("mysql", "password").getString("");

        HikariConfig config = new HikariConfig();
        config.setJdbcUrl("jdbc:mysql://" + host + ":" + port + "/" + database + "?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true");
        config.setUsername(username);
        config.setPassword(password);
        config.setMaximumPoolSize(10);
        config.setMinimumIdle(2);
        config.setConnectionTimeout(5000);
        config.setIdleTimeout(600000);
        config.setMaxLifetime(1800000);
        config.setPoolName("GoliathPool");

        dataSource = new HikariDataSource(config);

        GoliathPaperCore.getInstance().getLogger().info("MySQL connected.");
    }

    public Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }

    public void disconnect() {
        if (dataSource != null && !dataSource.isClosed()) {
            dataSource.close();
            GoliathPaperCore.getInstance().getLogger().info("MySQL connection closed!");
        }
    }
}
