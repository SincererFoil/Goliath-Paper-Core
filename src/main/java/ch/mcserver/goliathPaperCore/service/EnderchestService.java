package ch.mcserver.goliathPaperCore.service;

import ch.mcserver.goliathPaperCore.database.mongodb.repository.PlayerEnderchestRepository;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class EnderchestService {
    private final Map<UUID, Inventory> openInventories = new HashMap<>();


    private final PlayerEnderchestRepository enderchestRepository;
    public EnderchestService(PlayerEnderchestRepository enderchestRepository) {
        this.enderchestRepository = enderchestRepository;
    }

    public void openEnderchest(Player player) {
        if (!enderchestRepository.playerEnderchestExists(player.getUniqueId())) {
            enderchestRepository.createEnderchest(player.getUniqueId());
        }
        Inventory inventory = Bukkit.createInventory(new EnderchestHolder(player.getUniqueId()), 54, "Ender Chest");
        ItemStack[] itemsInEnderchest = enderchestRepository.loadEnderchest(player.getUniqueId());
        inventory.setContents(itemsInEnderchest);
        openInventories.put(player.getUniqueId(), inventory);
        player.openInventory(inventory);
    }
    public void removeOpenInventory(UUID uuid) {
        openInventories.remove(uuid);
    }

    public void saveAllOpenEnderchests() {
        for (Map.Entry<UUID, Inventory> entry : openInventories.entrySet()) {
            enderchestRepository.saveEnderchest(
                    entry.getKey(),
                    entry.getValue().getContents()
            );
        }

        openInventories.clear();
    }

}