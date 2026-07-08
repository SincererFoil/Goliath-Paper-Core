package ch.mcserver.goliathPaperCore.module.history.gui.specific;

import ch.mcserver.goliathPaperCore.module.history.HistoryEvent;
import ch.mcserver.goliathPaperCore.module.history.gui.ShowHistory;
import ch.mcserver.goliathPaperCore.module.history.gui.inventory.HistoryInventoryGui;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;

import java.util.List;

public class SpecificHistoryGuiListener implements Listener {

    @EventHandler
    public void onDrag(InventoryDragEvent event) {
        if (event.getInventory().getHolder() instanceof SpecificHistoryGuiHolder) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onClick(InventoryClickEvent event) {

        if (!(event.getInventory().getHolder() instanceof SpecificHistoryGuiHolder)) return;
        HumanEntity humanPlayer = event.getWhoClicked();
        Player player = (Player) humanPlayer;

        List<HistoryEvent> eventList = ((SpecificHistoryGuiHolder) event.getInventory().getHolder()).getEvents();
        HistoryEvent historyEvent = eventList.getFirst();

        switch (event.getRawSlot()) {
            case 11:
                player.sendMessage("Enderchest");
                event.setCancelled(true);
                break;
            case 12:
                event.getInventory().close();
                HistoryInventoryGui.openHistoryInventory(player, eventList);
                event.setCancelled(true);
                break;
            case 15:
                player.sendMessage("Homes");
                event.setCancelled(true);
                break;
            case 17:
                player.sendMessage("Rolled Back");
                event.setCancelled(true);
                break;
            case 18:
                ShowHistory.openHistory(player, historyEvent.uuid());
                event.setCancelled(true);
                break;
            default:
                event.setCancelled(true);
        }
    }
}
