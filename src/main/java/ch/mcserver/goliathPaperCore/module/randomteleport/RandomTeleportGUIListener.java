package ch.mcserver.goliathPaperCore.module.randomteleport;

import ch.mcserver.goliathPaperCore.GoliathPaperCore;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.plugin.Plugin;

public class RandomTeleportGUIListener implements Listener {

    private final RandomTeleportGUI randomTeleportGUI = new RandomTeleportGUI();


    private GoliathPaperCore plugin;

    public RandomTeleportGUIListener(GoliathPaperCore plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        Inventory inventory = event.getView().getTopInventory();

        if (!(inventory.getHolder() instanceof RandomTeleportInventoryHolder holder)) {
            return;
        }

        event.setCancelled(true);

        if (!(event.getWhoClicked() instanceof Player player)) {
            return;
        }

        if (event.getClickedInventory() != inventory) {
            return;
        }

        if (holder.isGeneralGUI()) {
            switch (event.getRawSlot()) {
                case 11 -> openOverworldGUI(player);
                case 13 -> player.sendMessage("Random Teleport to Nether");
                case 15 -> player.sendMessage("Random Teleport to End");
            }
        } else {
            switch (event.getRawSlot()) {
                case 10 -> player.sendMessage("Random Teleport to Na West");
                case 11 -> player.sendMessage("Random Teleport to Na East");
                case 12 -> player.sendMessage("Random Teleport to Eu West");
                case 13 -> player.sendMessage("Random Teleport to Eu Central");
                case 14 -> player.sendMessage("Random Teleport to Asia");
                case 15 -> player.sendMessage("Random Teleport to Oceania");
            }
        }
    }

    @EventHandler
    public void onInventoryDrag(InventoryDragEvent event) {
        if (event.getView().getTopInventory().getHolder() instanceof RandomTeleportInventoryHolder) {
            event.setCancelled(true);
        }
    }

    private void openOverworldGUI(Player player) {
        Bukkit.getScheduler().runTask(plugin, () ->
                randomTeleportGUI.openOverworldGUI(player)
        );
    }
}