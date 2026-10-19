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
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.Level;

public class RedisHeartbeatService {

    private BukkitTask task;

    private RedisManager redisManager;

    private Plugin plugin;

    private final AtomicBoolean heartbeatRunning = new AtomicBoolean(false);

    private static final String HEARTBEAT_KEY_PREFIX = "goliath:network:heartbeat:paper:";

    private final String instanceId = UUID.randomUUID().toString().substring(0, 8);

    private volatile ServerStatus serverStatus = ServerStatus.STARTING;

    public RedisHeartbeatService(RedisManager redisManager, Plugin plugin) {
        this.plugin = plugin;
        this.redisManager = redisManager;
    }


    public void start() {
        task = Bukkit.getScheduler().runTaskTimer(plugin, () -> {

            if (!heartbeatRunning.compareAndSet(false, true)) {
                return;
            }

            String players = String.valueOf(Bukkit.getOnlinePlayers().size());
            String tps = String.valueOf(Bukkit.getTPS()[0]);
            String mspt = String.valueOf(Bukkit.getAverageTickTime());
            String status = serverStatus.name();
            String timestamp = String.valueOf(System.currentTimeMillis());

            Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
                try (Jedis jedis = redisManager.getConnection()) {
                    String key = HEARTBEAT_KEY_PREFIX + GoliathPaperCore.serverName;

                    jedis.hset(key, Map.of(
                            "instance_id", instanceId,
                            "players", players,
                            "status", status,
                            "tps", tps,
                            "mspt", mspt,
                            "timestamp", timestamp
                    ));

                    jedis.expire(key, 15);

                } catch (Exception e) {
                    plugin.getLogger().log(Level.WARNING,
                            "Failed to send Paper heartbeat", e);
                } finally {
                    heartbeatRunning.set(false);
                }
            });

        }, 0L, 20L * 5);
    }

    public void stop() {
        if (task != null) {
            task.cancel();
        }
    }

    public ServerStatus getServerStatus() {
        return serverStatus;
    }

    public void setServerStatus(ServerStatus serverStatus) {
        this.serverStatus = serverStatus;
    }
}