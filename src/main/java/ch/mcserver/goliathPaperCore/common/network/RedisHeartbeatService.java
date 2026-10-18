package ch.mcserver.goliathPaperCore.common.network;

import ch.mcserver.goliathPaperCore.GoliathPaperCore;
import ch.mcserver.goliathPaperCore.common.database.redis.RedisManager;
import io.papermc.paper.threadedregions.scheduler.ScheduledTask;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitTask;
import redis.clients.jedis.Jedis;

import java.util.Map;
import java.util.UUID;
import java.util.logging.Level;

public class RedisHeartbeatService {

    private BukkitTask task;

    private RedisManager redisManager;

    private Plugin plugin;

    private static final String HEARTBEAT_KEY_PREFIX = "goliath:network:heartbeat:paper:";

    private final String instanceId = UUID.randomUUID().toString().substring(0, 8);

    private ServerStatus serverStatus = ServerStatus.STARTING;

    public RedisHeartbeatService(RedisManager redisManager, Plugin plugin) {
        this.plugin = plugin;
        this.redisManager = redisManager;
    }


    public void start() {
        task = Bukkit.getScheduler().runTaskTimer(plugin, () -> {
            try (Jedis jedis = redisManager.getConnection()) {
                jedis.hset(HEARTBEAT_KEY_PREFIX + GoliathPaperCore.serverName, Map.of(
                        "instance_id", instanceId,
                        "players", String.valueOf(Bukkit.getOnlinePlayers().size()),
                        "status", serverStatus.toString(),
                        "tps", String.valueOf(Bukkit.getTPS()[0]),
                        "mspt", String.valueOf(Bukkit.getAverageTickTime()),
                        "timestamp", String.valueOf(System.currentTimeMillis())
                ));
            } catch (Exception e) {
                plugin.getLogger().log(Level.WARNING, "Wasn't able to connect to Redis", e);
            }
        }, 0L, 20L * 2);
    }

    public void stop() {
        task.cancel();
    }

    public ServerStatus getServerStatus() {
        return serverStatus;
    }

    public void setServerStatus(ServerStatus serverStatus) {
        this.serverStatus = serverStatus;
    }
}