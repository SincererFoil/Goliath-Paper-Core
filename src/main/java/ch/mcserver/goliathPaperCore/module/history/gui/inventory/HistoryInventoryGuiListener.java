package ch.mcserver.goliathPaperCore.module.history.gui.inventory;

import ch.mcserver.goliathPaperCore.module.history.gui.HistoryEvent;
import ch.mcserver.goliathPaperCore.module.history.gui.specific.GUI;
import ch.mcserver.goliathPaperCore.module.history.gui.specific.SpecificHistoryGuiHolder;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;

import java.util.List;
import java.util.UUID;

public class HistoryInventoryGuiListener implements Listener {

    @EventHandler
    public void onDrag(InventoryDragEvent event) {
        if (!(event.getInventory().getHolder() instanceof SpecificHistoryInventoryGuiHolder)) return;

        HumanEntity humanEntity = event.getWhoClicked();
        if (!(humanEntity instanceof Player)) return;

        Player player = (Player)  humanEntity;

        if (!player.hasPermission("goliath.*")) {
            event.setCancelled(true);
        }



    }

    @EventHandler
    public void onClick(InventoryClickEvent event) {

        if (!(event.getInventory().getHolder() instanceof SpecificHistoryInventoryGuiHolder holder)) return;

        List<HistoryEvent> eventList = holder.getEvents();

        HistoryEvent historyEvent = eventList.getFirst();

        HumanEntity humanEntity = event.getWhoClicked();

        if (!(humanEntity instanceof Player)) return;

        Player player = (Player)  humanEntity;

        if (event.getRawSlot() == 36) {
            event.getInventory().close();
            event.setCancelled(true);
            GUI.openSpecificInventory(player, UUID.fromString(historyEvent.historyId()));
        }
    }
}
