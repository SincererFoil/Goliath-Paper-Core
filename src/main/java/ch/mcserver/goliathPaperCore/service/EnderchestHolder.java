package ch.mcserver.goliathPaperCore.service;

import ch.mcserver.goliathPaperCore.mongodb.repository.PlayerEnderchestRepository;
import org.bukkit.block.EnderChest;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class EnderchestHolder implements InventoryHolder {

    public static HashMap<UUID, EnderChest> openedEnderChests = new HashMap<>();

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