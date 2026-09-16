package ch.mcserver.goliathPaperCore.module.anticheat.inspection.gui;

import ch.mcserver.goliathPaperCore.module.anticheat.inspection.player.SuspectEntry;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class SusGuiHolder implements InventoryHolder {

    private final List<SuspectEntry> suspects;

    private final int page;

    private Inventory inventory;

    public SusGuiHolder(List<SuspectEntry> suspects, int page) {
        this.suspects = suspects;
        this.page = page;
    }


    @Override
    public @NotNull Inventory getInventory() {
        return inventory;
    }

    public void setInventory(Inventory inventory) {
        this.inventory = inventory;
    }

    public int getPage() {
        return page;
    }

    public int getTotalPages() {
        return Math.max(1, (int) Math.ceil(suspects.size() / (double) SusGui.PLAYERS_PER_PAGE));
    }

    public List<SuspectEntry> getSuspects() {
        return  suspects;
    }

}
