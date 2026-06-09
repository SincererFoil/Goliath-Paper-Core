package ch.mcserver.goliathPaperCore.service;

import ch.mcserver.goliathPaperCore.GoliathPaperCore;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

public class EnderchestService {

    public void openEnderchest(Player player) {
        Inventory inventory = Bukkit.createInventory(null, 54, "Ender Chest");
        player.openInventory(inventory);
    }
}
