package ch.mcserver.goliathPaperCore.module.history.HistorySnapshot;

import ch.mcserver.goliathPaperCore.GoliathPaperCore;
import ch.mcserver.goliathPaperCore.common.database.mongodb.HistoryRepository;
import ch.mcserver.goliathPaperCore.common.database.mysql.PlayerObject;
import ch.mcserver.goliathPaperCore.common.database.mysql.PlayerObjectManager;
import ch.mcserver.goliathPaperCore.module.inventory.InventoryData;
import ch.mcserver.goliathPaperCore.module.inventory.PlayerInventoryRepository;

import java.util.UUID;

public class HistorySnapshot {

    public static void createSnapshot(UUID playerUUID, UUID historyUUID, String type) {

        if (!PlayerObjectManager.hasPlayer(playerUUID)) {
            GoliathPaperCore.getInstance().getLogger().warning("Player " + playerUUID.toString() + " does not exist!");
            return;
        }

        InventoryData inventoryData = GoliathPaperCore.getPlayerInventoryRepository().getInventoryData(playerUUID);

        if (inventoryData == null) {
            return;
        }




    }

}
