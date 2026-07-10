package ch.mcserver.goliathPaperCore.common.database.redis;

import ch.mcserver.goliathPaperCore.GoliathPaperCore;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.logging.Level;

public class RedisManager {

    private JedisPool pool;

    public void connect() {
        String ipAddress = GoliathPaperCore.config.node("redis", "ipaddress").getString();
        int port = GoliathPaperCore.config.node("redis", "port").getInt();
        String password =  GoliathPaperCore.config.node("redis", "password").getString();
        try {
            if (password == null || password.isBlank()) {
                pool = new JedisPool(ipAddress, port);
                GoliathPaperCore.getInstance().getLogger().log(Level.INFO, "Connected to Redis.");
            } else {
                pool = new JedisPool(ipAddress, port, null, password);
                GoliathPaperCore.getInstance().getLogger().log(Level.INFO, "Connected to Redis.");
            }
        } catch (Exception e) {
            GoliathPaperCore.getInstance().getLogger().log(Level.WARNING, e.getMessage());
        }
    }

    public Jedis getConnection() {
        return pool.getResource();
    }

    public void close() {
        if (pool != null) {
            pool.close();
            GoliathPaperCore.getInstance().getLogger().log(Level.INFO, "Closed Redis.");
        }
    }
}

