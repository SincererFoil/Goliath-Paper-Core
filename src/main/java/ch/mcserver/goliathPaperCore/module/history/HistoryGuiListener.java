package ch.mcserver.goliathPaperCore.module.history;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.InventoryHolder;

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
    }
}