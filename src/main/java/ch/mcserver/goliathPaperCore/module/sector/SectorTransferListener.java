
package ch.mcserver.goliathPaperCore.module.sector;

import ch.mcserver.goliathPaperCore.GoliathPaperCore;
import ch.mcserver.goliathPaperCore.common.database.redis.RedisManager;
import com.google.gson.Gson;
import io.papermc.paper.event.player.AsyncPlayerSpawnLocationEvent;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerPortalEvent;
import redis.clients.jedis.Jedis;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Level;

public class SectorTransferListener implements Listener {

    private static final String CHANNEL = "goliath:sector:transfer:pending:";

    private static final String DELETE_IF_MATCHES = """
            if redis.call('GET', KEYS[1]) == ARGV[1] then
                return redis.call('DEL', KEYS[1])
            end
            return 0
            """;

    private final GoliathPaperCore plugin;

    private final RedisManager redisManager;

    private final Gson gson = new Gson();

    private final Map<World.Environment, World> worlds = new EnumMap<>(World.Environment.class);


    public SectorTransferListener(GoliathPaperCore plugin, RedisManager redisManager) {
        this.plugin = plugin;
        this.redisManager = redisManager;

        for (World world : Bukkit.getWorlds()) {
            worlds.putIfAbsent(world.getEnvironment(), world);
        }
    }


    @EventHandler
    public void onAsyncPlayerSpawnLocation(AsyncPlayerSpawnLocationEvent event) {
        UUID playerUuid = event.getConnection().getProfile().getId();

        if (playerUuid == null) return;

        String key = CHANNEL + playerUuid;

        try (Jedis jedis = redisManager.getConnection()) {

            String json = jedis.get(key);

            if (json == null) return;

            SectorTransferData data = gson.fromJson(json, SectorTransferData.class);

            if (data == null || data.transferUuid() == null || data.playerUuid() == null || data.targetServer() == null || data.dimension() == null) return;

            if (!playerUuid.equals(data.playerUuid()) || !GoliathPaperCore.serverName.equals(data.targetServer())) return;

            if (!Double.isFinite(data.x()) || !Double.isFinite(data.y()) || !Double.isFinite(data.z()) || !Float.isFinite(data.yaw()) || !Float.isFinite(data.pitch())) return;

            World.Environment environment = World.Environment.valueOf(data.dimension());

            World world = worlds.get(environment);

            if (world == null) return;

            Location location = new Location(world, data.x(), data.y(), data.z(), data.yaw(), data.pitch());

            Object deleted = jedis.eval(DELETE_IF_MATCHES, List.of(key), List.of(json));

            if (!(deleted instanceof Number) || ((Number) deleted).longValue() != 1L) return;

            event.setSpawnLocation(location);

        } catch (Exception e) {
            plugin.getLogger().log(Level.SEVERE, "Failed to load sector transfer", e);
        }
    }

    @EventHandler
    public void onPlayerPortalEvent(PlayerPortalEvent event) {
        event.setCancelled(true);
    }
}
