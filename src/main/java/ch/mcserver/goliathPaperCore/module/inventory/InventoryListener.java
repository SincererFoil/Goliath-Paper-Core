package ch.mcserver.goliathPaperCore.module.inventory;

import ch.mcserver.goliathPaperCore.GoliathPaperCore;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.player.*;

public class InventoryListener implements Listener {

    private final PlayerInventoryRepository repository;

    public InventoryListener(PlayerInventoryRepository repository) {
        this.repository = repository;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        player.getInventory().clear();
        repository.loadInventory(player.getUniqueId(), 0);
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        repository.saveInventory(event.getPlayer().getUniqueId(), true);
    }

    @EventHandler
    public void onClick(InventoryClickEvent event) {
        if (event.getWhoClicked() instanceof Player player && !repository.isLoaded(player)) {
            player.sendActionBar(Component.text("Your data hasn't loaded yet", NamedTextColor.RED));
            player.playSound(player, Sound.ENTITY_VILLAGER_AMBIENT, 1f, 1f);
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onInteractEntity(PlayerInteractEntityEvent event) {
        if (!repository.isLoaded(event.getPlayer())) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onDrop(PlayerDropItemEvent event) {
        if (!repository.isLoaded(event.getPlayer())) {
            event.getPlayer().sendActionBar(Component.text("Your data hasn't loaded yet", NamedTextColor.RED));
            event.getPlayer().playSound(event.getPlayer().getLocation(), Sound.ENTITY_VILLAGER_AMBIENT, 1f, 1f);
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onDrag(InventoryDragEvent event) {
        if (event.getWhoClicked() instanceof Player player && !repository.isLoaded(player)) {
            player.sendActionBar(Component.text("Your data hasn't loaded yet", NamedTextColor.RED));
            player.playSound(player, Sound.ENTITY_VILLAGER_AMBIENT, 1f, 1f);
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onPickup(EntityPickupItemEvent event) {
        if (event.getEntity() instanceof Player player && !repository.isLoaded(player)) {
            player.sendActionBar(Component.text("Your data hasn't loaded yet", NamedTextColor.RED));
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        if (!repository.isLoaded(event.getPlayer())) {
            event.getPlayer().sendActionBar(Component.text("Your data hasn't loaded yet", NamedTextColor.RED));
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onSwap(PlayerSwapHandItemsEvent event) {
        if (!repository.isLoaded(event.getPlayer())) {
            event.getPlayer().sendActionBar(Component.text("Your data hasn't loaded yet", NamedTextColor.RED));
            event.getPlayer().playSound(event.getPlayer().getLocation(), Sound.ENTITY_VILLAGER_AMBIENT, 1f, 1f);
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onDamage(EntityDamageEvent event) {
        if (event.getEntity() instanceof Player player && !repository.isLoaded(player)) {
            player.sendActionBar(Component.text("Your data hasn't loaded yet", NamedTextColor.RED));
            event.setCancelled(true);
        }
    }
}
