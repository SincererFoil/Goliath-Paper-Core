package ch.mcserver.goliathPaperCore.module.history;

import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.UUID;

public class HistoryGuiHolder implements InventoryHolder {

    private final UUID targetUUID;
    private final List<HistoryEvent> events;
    private final int page;
    private Inventory inventory;

    public  HistoryGuiHolder(UUID targetUUID, List<HistoryEvent> events, int page) {
        this.targetUUID = targetUUID;
        this.events = events;
        this.page = page;
    }

    @NotNull
    @Override
    public Inventory getInventory() {
        return  inventory;
    }

    public void setInventory(Inventory inventory) {
        this.inventory = inventory;
    }

    public  UUID getTargetUUID() {
        return targetUUID;
    }

    public List<HistoryEvent> getEvents() {
        return events;
    }

    public int getPage() {
        return page;
    }

    public int getTotalPages() {
        return Math.max(1, (int) Math.ceil(events.size() / (double) ShowHistory.EVENTS_PER_PAGE));
    }
}
