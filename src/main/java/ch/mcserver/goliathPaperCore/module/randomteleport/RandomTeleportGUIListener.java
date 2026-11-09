package ch.mcserver.goliathPaperCore.module.randomteleport;

import ch.mcserver.goliathPaperCore.GoliathPaperCore;
import org.bukkit.Bukkit;
import org.bukkit.World;
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

    private final RandomTeleportService randomTeleportService;

    public RandomTeleportGUIListener(GoliathPaperCore plugin, RandomTeleportService randomTeleportService) {
        this.plugin = plugin;
        this.randomTeleportService = randomTeleportService;
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
                case 13 -> {
                    randomTeleportService.requestRandomTeleport(new RTPRequest(player, Region.NA_EAST, World.Environment.NETHER));
                    player.closeInventory();
                }
                case 15 -> {
                    randomTeleportService.requestRandomTeleport(new RTPRequest(player, Region.NA_EAST, World.Environment.THE_END));
                    player.closeInventory();
                }
            }
        } else {
            Region region = switch (event.getRawSlot()) {
                case 10 -> Region.NA_WEST;
                case 11 -> Region.NA_EAST;
                case 12 -> Region.EU_WEST;
                case 13 -> Region.EU_CENTRAL;
                case 14 -> Region.ASIA;
                case 15 -> Region.OCEANIA;
                default -> null;
            };

            if (region == null) return;

            randomTeleportService.requestRandomTeleport(new RTPRequest(player, region, World.Environment.NORMAL));
            player.closeInventory();
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