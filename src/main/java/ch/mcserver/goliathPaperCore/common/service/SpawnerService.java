package ch.mcserver.goliathPaperCore.common.service;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.SpawnerSpawnEvent;
import org.bukkit.event.player.PlayerJoinEvent;

public class SpawnerService implements Listener {

    @EventHandler
    public void onSpawnerSpawn(SpawnerSpawnEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        event.setJoinMessage(null);
    }
}
