package ch.mcserver.goliathPaperCore.module.history.gui.inventory;

import ch.mcserver.goliathPaperCore.module.history.HistoryEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

import java.util.List;

public class SpecificHistoryInventoryGuiHolder implements InventoryHolder {

    private final List<HistoryEvent> events;
    private Inventory inventory;

    public SpecificHistoryInventoryGuiHolder(List<HistoryEvent> events) {
        this.events = events;
    }

    @Override
    public Inventory getInventory() {
        return inventory;
    }

    public void setInventory(Inventory inventory) {
        this.inventory = inventory;
    }

    public List<HistoryEvent> getEvents() {
        return events;
    }
}
