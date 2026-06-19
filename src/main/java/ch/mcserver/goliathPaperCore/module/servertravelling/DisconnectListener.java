package ch.mcserver.goliathPaperCore.module.servertravelling;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class DisconnectListener implements Listener {
    @EventHandler
    public void onPlayerDisconnect(PlayerQuitEvent event){
        Player player = event.getPlayer();
        Location location = player.getLocation();
        // Set Spawn loc

    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event){
        Player player = event.getPlayer();
        Location location = player.getLocation();
        // Teleport to Spawn loc

    }
}
