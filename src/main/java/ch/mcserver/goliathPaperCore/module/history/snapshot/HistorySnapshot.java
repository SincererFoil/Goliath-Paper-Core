package ch.mcserver.goliathPaperCore.module.history.snapshot;

import ch.mcserver.goliathPaperCore.GoliathPaperCore;
import ch.mcserver.goliathPaperCore.common.database.mongodb.HistoryRepository;
import ch.mcserver.goliathPaperCore.common.database.mysql.PlayerObject;
import ch.mcserver.goliathPaperCore.common.database.mysql.PlayerObjectManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;

import static ch.mcserver.goliathPaperCore.module.enderchest.PlayerEnderchestRepository.toBase64;

public class HistorySnapshot {

    public static void createSnapshot(UUID playerUUID, UUID historyUUID, String type) {

        if (!PlayerObjectManager.hasPlayer(playerUUID)) {
            GoliathPaperCore.getInstance().getLogger().warning("Player " + playerUUID.toString() + " does not exist!");
            return;
        }

        Player player = Bukkit.getPlayer(playerUUID);

        if (player == null) {
            return;
        }

        String inventory = toBase64(player.getInventory().getContents());
        String armor = toBase64(player.getInventory().getArmorContents());
        String offhand = toBase64(new ItemStack[]{player.getInventory().getItemInOffHand()});

        GoliathPaperCore.getPlayerInventorySnapshotRepository().createInventorySnapshot(playerUUID, historyUUID, inventory, armor, offhand);

    }

}
