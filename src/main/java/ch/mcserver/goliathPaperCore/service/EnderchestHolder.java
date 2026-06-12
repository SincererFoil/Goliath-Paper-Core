package ch.mcserver.goliathPaperCore.service;

import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

import java.util.UUID;

public class EnderchestHolder implements InventoryHolder {

    private final UUID owner;

    public EnderchestHolder(UUID owner) {
        this.owner = owner;
    }

    public UUID getOwner() {
        return owner;
    }

    @Override
    public Inventory getInventory() {
        return null;
    }
}