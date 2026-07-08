package ch.mcserver.goliathPaperCore.module.history.gui;

import ch.mcserver.goliathPaperCore.module.history.gui.specific.SpecificHistoryGui;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;
import java.util.UUID;

public class HistoryGuiListener implements Listener {

    @EventHandler
    public void onClick(InventoryClickEvent event) {
        InventoryHolder holder = event.getInventory().getHolder();

        if (!(holder instanceof HistoryGuiHolder historyGuiHolder)) {
            return;
        }

        event.setCancelled(true);

        if (!(event.getWhoClicked() instanceof Player player)) {
            return;
        }

        int slot = event.getRawSlot();

        if (slot == ShowHistory.SLOT_PREV && historyGuiHolder.getPage() > 0) {
            ShowHistory.openPage(player, historyGuiHolder.getTargetUUID(),
                    historyGuiHolder.getEvents(), historyGuiHolder.getPage() - 1);
            return;
        }

        if (slot == ShowHistory.SLOT_NEXT && historyGuiHolder.getPage() < historyGuiHolder.getTotalPages() - 1) {
            ShowHistory.openPage(player, historyGuiHolder.getTargetUUID(),
                    historyGuiHolder.getEvents(), historyGuiHolder.getPage() + 1);
        }

        ItemStack item = event.getCurrentItem();

        if (item == null || item.getType() == Material.AIR) return;

        ItemMeta meta = item.getItemMeta();

        if (meta == null || !meta.hasLore()) return;
        List<String> lore = meta.getLore();
        UUID historyUUID = null;

        for (String line : lore) {
            String stripped = ChatColor.stripColor(line);

            if (stripped.startsWith("Bal")) {
                String[] words = stripped.split(" ");
                String uuidString = words[words.length - 1];

                try {
                    historyUUID = UUID.fromString(uuidString);
                } catch (IllegalArgumentException e) {
                }
                break;
            }
        }

        if (historyUUID != null) {
            event.getInventory().close();
            SpecificHistoryGui.openSpecificInventory(player, historyUUID);
        }
    }
}
