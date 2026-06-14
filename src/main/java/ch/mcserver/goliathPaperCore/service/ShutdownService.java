package ch.mcserver.goliathPaperCore.service;

import ch.mcserver.goliathPaperCore.database.mongodb.MongoDBManager;
import ch.mcserver.goliathPaperCore.database.mongodb.repository.PlayerInventoryRepository;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.logging.Level;
import java.util.logging.Logger;

public class ShutdownService {

    private final Logger logger;
    private final MongoDBManager mongoManager;
    private final EnderchestService enderchestService;
    private final PlayerInventoryRepository playerInventoryRepository;


    public ShutdownService(Logger logger, MongoDBManager mongoManager, EnderchestService enderchestService, PlayerInventoryRepository playerInventoryRepository) {

        this.logger = logger;
        this.mongoManager = mongoManager;
        this.enderchestService = enderchestService;
        this.playerInventoryRepository = playerInventoryRepository;
    }

    public void shutdown() {
        saveOnlineInventories();
        saveOpenEnderchests();

        if (mongoManager != null) {
            mongoManager.disconnect();
        }
    }

    private void saveOnlineInventories() {
        if (playerInventoryRepository == null) return;

        for (Player player : Bukkit.getOnlinePlayers()) {
            try {
                playerInventoryRepository.saveInventory(player.getUniqueId());
            } catch (Exception e) {
                logger.log(Level.WARNING, "Could not save inventory from " + player.getName(), e);
            }
        }
    }

    private void saveOpenEnderchests() {
        if (enderchestService == null) return;

        try {
            enderchestService.saveAllOpenEnderchests();
        } catch (Exception e) {
            logger.log(Level.WARNING, "Could not save open enderchests", e);
        }
    }
}