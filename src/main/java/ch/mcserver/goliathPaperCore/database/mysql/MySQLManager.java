package ch.mcserver.goliathPaperCore.database.mysql;

import ch.mcserver.goliathPaperCore.GoliathPaperCore;

import java.sql.Connection;
import java.sql.DriverManager;

public class MySQLManager {

    private Connection connection;


    public synchronized void connect() {

        try {

            String host = GoliathPaperCore.config.node("mysql", "host").getString("127.0.0.1");
            int port = GoliathPaperCore.config.node("mysql", "port").getInt(3306);
            String database = GoliathPaperCore.config.node("mysql", "database").getString("goliath");
            String username = GoliathPaperCore.config.node("mysql", "username").getString("goliath");
            String password = GoliathPaperCore.config.node("mysql", "password").getString("");
            Class.forName("com.mysql.cj.jdbc.Driver");

            connection = DriverManager.getConnection(
                    "jdbc:mysql://" + host + ":" + port + "/" + database +
                            "?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true",
                    username,
                    password
            );


            GoliathPaperCore.getInstance().logger.info("[Goliath] MySQL connected.");

        } catch (Exception exception) {

            GoliathPaperCore.getInstance().logger.warning("[Goliath] MySQL connection failed.");
            exception.printStackTrace();
        }
    }

    public synchronized Connection getConnection() {

        try {
            if (connection == null || connection.isClosed() || !connection.isValid(3)) {
                connect();
            }

            return connection;

        } catch (Exception exception) {
            GoliathPaperCore.getInstance().logger.warning("[Goliath] MySQL connection check failed.");
            connect();
            return connection;
        }
    }

    public void disconnect() {

        try {

            if (connection != null && !connection.isClosed()) {
                connection.close();
                GoliathPaperCore.getInstance().logger.info("[Goliath] MySQL connection closed!");
            }

        } catch (Exception exception) {
            exception.printStackTrace();
            GoliathPaperCore.getInstance().logger.warning("[Goliath]  Crashed! Reason: " + exception.getMessage());
        }
    }
}
