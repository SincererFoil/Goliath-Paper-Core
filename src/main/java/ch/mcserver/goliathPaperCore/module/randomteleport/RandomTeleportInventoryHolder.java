package ch.mcserver.goliathPaperCore.module.randomteleport;

import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class RandomTeleportInventoryHolder implements InventoryHolder {

    private Inventory inventory;

    private final UUID targetUUID;

    private final boolean isGeneralGUI;


    public RandomTeleportInventoryHolder(UUID targetUUID, boolean isGeneralGUI) {
        this.targetUUID = targetUUID;
        this.isGeneralGUI = isGeneralGUI;
    }

    @Override
    public @NotNull Inventory getInventory() {
        return inventory;
    }

    public void setInventory(Inventory inventory) {
        this.inventory = inventory;
    }

    public boolean isGeneralGUI() {
        return isGeneralGUI;
    }

}
