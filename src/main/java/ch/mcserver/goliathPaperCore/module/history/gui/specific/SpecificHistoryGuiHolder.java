package ch.mcserver.goliathPaperCore.module.history.gui.specific;

import ch.mcserver.goliathPaperCore.module.history.gui.HistoryEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

import java.util.List;

public class SpecificHistoryGuiHolder implements InventoryHolder {

    private final List<HistoryEvent> events;
    private Inventory inventory;

    public SpecificHistoryGuiHolder(List<HistoryEvent> events) {
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
