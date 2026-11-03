package ch.mcserver.goliathPaperCore.module.randomteleport;

import ch.mcserver.goliathPaperCore.GoliathPaperCore;
import ch.mcserver.goliathPaperCore.common.database.redis.RedisManager;
import com.google.gson.Gson;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import redis.clients.jedis.Jedis;

public class RandomTeleportService {

    private final RedisManager redisManager;

    private final GoliathPaperCore plugin;

    private final Gson gson = new Gson();


    public RandomTeleportService(RedisManager redisManager, GoliathPaperCore plugin) {
        this.redisManager = redisManager;
        this.plugin = plugin;
    }

    public void requestRandomTeleport(RTPRequest request) {

        BukkitRunnable runnable = new BukkitRunnable() {
            @Override
            public void run() {
                executeRandomTeleport(request);
            }
        };
        GoliathPaperCore.getInstance().getTimerUtil().executeWithTimer(5, runnable, request.getPlayer(), "Teleporting in");
    }

    private void executeRandomTeleport(RTPRequest request) {
        Player player = request.getPlayer();
        player.sendActionBar(Component.text("#1 in Queue", NamedTextColor.GRAY));
        String json = gson.toJson(request);

        try {
            redisManager.publish("goliath:network:rtp:request", json);
        } catch (Exception e) {
            player.playSound(player, Sound.ENTITY_VILLAGER_AMBIENT, 1.0f, 1.0f);
            player.sendMessage(Component.text("The RTP is currently restarting, Please wait a few minutes and try again.", NamedTextColor.RED));
        }
    }



}
