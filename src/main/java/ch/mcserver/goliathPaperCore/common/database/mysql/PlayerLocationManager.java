package ch.mcserver.goliathPaperCore.common.database.mysql;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.plugin.Plugin;

import java.sql.Timestamp;

public class PlayerLocationManager implements Listener {

    private final Plugin plugin;
    private final PlayerLocationRepository repository;
    private final String serverName;

    public PlayerLocationManager(Plugin plugin, PlayerLocationRepository repository, String serverName) {
        this.plugin = plugin;
        this.repository = repository;
        this.serverName = serverName;

        Bukkit.getPluginManager().registerEvents(this, plugin);

        startAutoSaveTask();
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        saveLocation(event.getPlayer());
    }

    @EventHandler
    public void onTeleport(PlayerTeleportEvent event) {
        Bukkit.getScheduler().runTaskLater(plugin, () -> {
            saveLocation(event.getPlayer());
        }, 1L);
    }

    @EventHandler
    public void onChangedWorld(PlayerChangedWorldEvent event) {
        Bukkit.getScheduler().runTaskLater(plugin, () -> {
            saveLocation(event.getPlayer());
        }, 1L);
    }

    private void startAutoSaveTask() {
        Bukkit.getScheduler().runTaskTimer(plugin, () -> {
            for (Player player : Bukkit.getOnlinePlayers()) {
                saveLocation(player);
            }
        }, 20L * 5, 20L * 5);
    }

    public void saveLocation(Player player) {
        if (player == null || !player.isOnline()) {
            return;
        }

        Location location = player.getLocation();

        PlayerLocationObject locationObject = new PlayerLocationObject(
                player.getUniqueId(),
                new Timestamp(System.currentTimeMillis()),
                location.getYaw(),
                location.getPitch(),
                location.getBlockX(),
                location.getBlockY(),
                location.getBlockZ(),
                serverName
        );

        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
            repository.save(locationObject);
        });
    }
}