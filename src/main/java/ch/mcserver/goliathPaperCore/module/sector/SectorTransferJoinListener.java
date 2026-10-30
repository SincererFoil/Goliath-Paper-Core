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
import redis.clients.jedis.Jedis;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.logging.Level;


public class SectorTransferJoinListener implements Listener {

    private final GoliathPaperCore plugin;

    private final RedisManager redisManager;

    private final Gson gson = new Gson();

    private final static String CHANNEL = "goliath:sector:transfer:pending:";

    private final Map<World.Environment, World> worlds = new EnumMap<>(World.Environment.class);


    public SectorTransferJoinListener(GoliathPaperCore plugin, RedisManager redisManager) {
        this.plugin = plugin;
        this.redisManager = redisManager;


        for (World world : Bukkit.getWorlds()) {
            worlds.putIfAbsent(world.getEnvironment(), world);
        }
    }





    @EventHandler
    public void onAsyncPlayerSpawnLocation(AsyncPlayerSpawnLocationEvent event) {
        try (Jedis jedis = redisManager.getConnection()) {
            String json = jedis.get(
                    CHANNEL + event.getConnection().getProfile().getId()
            );


            if (json == null) return;

            SectorTransferData data = gson.fromJson(json, SectorTransferData.class);

            if (data == null || data.playerUuid() == null || data.targetServer() == null || data.dimension() == null || !data.playerUuid().equals(event.getConnection().getProfile().getId()) || !Double.isFinite(data.x()) || !Double.isFinite(data.y()) || !Double.isFinite(data.z()) || !Float.isFinite(data.yaw()) || !Float.isFinite(data.pitch())) return;

            if (data == null || !GoliathPaperCore.serverName.equals(data.targetServer())) {
                return;
            }

            World.Environment environment = World.Environment.valueOf(data.dimension());

            World world = worlds.get(environment);
            if (world == null) return;

            Location location = new Location(world, data.x(), data.y(), data.z(), data.yaw(), data.pitch());

            event.setSpawnLocation(location);

            jedis.del(CHANNEL + data.playerUuid());


        } catch (Exception e) {
            plugin.getLogger().log(Level.SEVERE, "Failed to load sector transfer", e);
        }
    }
}