package ch.mcserver.goliathPaperCore.module.randomteleport;

import ch.mcserver.goliathPaperCore.GoliathPaperCore;
import ch.mcserver.goliathPaperCore.common.database.redis.RedisManager;
import ch.mcserver.goliathPaperCore.common.service.TimerUtil;
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

    private TimerUtil timerUtil;


    public RandomTeleportService(RedisManager redisManager, GoliathPaperCore plugin, TimerUtil timerUtil) {
        this.redisManager = redisManager;
        this.plugin = plugin;
        this.timerUtil = timerUtil;
    }

    public void requestRandomTeleport(RTPRequest request) {

        BukkitRunnable runnable = new BukkitRunnable() {
            @Override
            public void run() {
                executeRandomTeleport(request);
            }
        };
        timerUtil.executeWithTimer(5, runnable, request.getPlayerUuid(), "Teleporting in");
    }

    private void executeRandomTeleport(RTPRequest request) {
        Player player = Bukkit.getPlayer(request.getPlayerUuid());

        if (player == null) {
            return;
        }

        try {
            String json = gson.toJson(request);
            redisManager.publish("goliath:rtp:request", json);
            player.sendActionBar(Component.text("Teleporting ...", NamedTextColor.GRAY));
        } catch (Exception e) {
            player.playSound(player, Sound.ENTITY_VILLAGER_AMBIENT, 1.0f, 1.0f);
            player.sendMessage(Component.text("The RTP is currently in maintenance, Please wait a few minutes and try again.", NamedTextColor.RED));
        }
    }



}
