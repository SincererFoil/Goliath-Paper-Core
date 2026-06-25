package ch.mcserver.goliathPaperCore.module.randomteleport;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;

import static ch.mcserver.goliathPaperCore.module.randomteleport.OverworldGUI.openOverworldGUI;

public class RandomTeleportMainListener implements Listener {

    private static final String GUI_TITLE = "Random Teleport";

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (!event.getView().getTitle().equals(GUI_TITLE)) {
            return;
        }

        event.setCancelled(true);

        switch (event.getRawSlot()) {
            case 11:
                if (event.getCurrentItem() == null
                        || !event.getCurrentItem().hasItemMeta()
                        || !event.getCurrentItem().getItemMeta().hasDisplayName()
                        || !event.getCurrentItem().getItemMeta().getDisplayName().equals(ChatColor.WHITE + "Overworld")) {
                    return;
                }

                openOverworldGUI((Player) event.getWhoClicked());
                break;

            case 13:
                if (event.getCurrentItem() == null
                        || !event.getCurrentItem().hasItemMeta()
                        || !event.getCurrentItem().getItemMeta().hasDisplayName()
                        || !event.getCurrentItem().getItemMeta().getDisplayName().equals("Nether")) {
                    return;
                }

                break;

            case 15:
                if (event.getCurrentItem() == null
                        || !event.getCurrentItem().hasItemMeta()
                        || !event.getCurrentItem().getItemMeta().hasDisplayName()
                        || !event.getCurrentItem().getItemMeta().getDisplayName().equals("End")) {
                    return;
                }

                break;
            default:
                return;
        }
    }

    @EventHandler
    public void onInventoryDrag(InventoryDragEvent event) {
        if (!event.getView().getTitle().equals(GUI_TITLE)) {
            return;
        }

        event.setCancelled(true);
    }
}