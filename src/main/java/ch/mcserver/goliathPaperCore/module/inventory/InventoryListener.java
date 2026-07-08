package ch.mcserver.goliathPaperCore.module.inventory;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class InventoryListener implements Listener {

    private final PlayerInventoryRepository repository;

    public InventoryListener(PlayerInventoryRepository repository) {
        this.repository = repository;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        repository.loadInventory(player.getUniqueId());
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        repository.saveInventory(event.getPlayer().getUniqueId());
    }
}
