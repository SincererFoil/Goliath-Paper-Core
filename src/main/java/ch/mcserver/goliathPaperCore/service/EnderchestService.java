package ch.mcserver.goliathPaperCore.service;

import ch.mcserver.goliathPaperCore.mongodb.repository.PlayerEnderchestRepository;
import org.bson.Document;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class EnderchestService {

    private final PlayerEnderchestRepository enderchestRepository;
    public EnderchestService(PlayerEnderchestRepository enderchestRepository) {
        this.enderchestRepository = enderchestRepository;
    }

    public void openEnderchest(Player player) {
        if (!enderchestRepository.playerEnderchestExists(player.getUniqueId())) {
            enderchestRepository.createEnderchest(player.getUniqueId());
        }
        Inventory inventory = Bukkit.createInventory(new EnderchestHolder(player.getUniqueId()), 54, "Ender Chest");
        if (enderchestRepository.playerEnderchestExists(player.getUniqueId())) {
            enderchestRepository.createEnderchest(player.getUniqueId());
        }
        ItemStack[] itemsInEnderchest = enderchestRepository.loadEnderchest(player.getUniqueId());
        inventory.setContents(itemsInEnderchest);
        player.openInventory(inventory);
    }
}