package ch.mcserver.goliathPaperCore;

import ch.mcserver.goliathPaperCore.database.mongodb.MongoDBManager;
import ch.mcserver.goliathPaperCore.database.mongodb.repository.PlayerEnderchestRepository;
import ch.mcserver.goliathPaperCore.database.mongodb.repository.PlayerInventoryRepository;
import ch.mcserver.goliathPaperCore.database.mysql.MySQLManager;
import ch.mcserver.goliathPaperCore.listener.EnderchestListener;
import ch.mcserver.goliathPaperCore.listener.InventoryListener;
import ch.mcserver.goliathPaperCore.listener.SpawnListener;
import ch.mcserver.goliathPaperCore.pluginmessenger.CommandUpdateMessenger;
import ch.mcserver.goliathPaperCore.pluginmessenger.GmspMessenger;
import ch.mcserver.goliathPaperCore.pluginmessenger.GoliathTeleportMessenger;
import ch.mcserver.goliathPaperCore.pluginmessenger.HistorySnapshotMessenger;
import ch.mcserver.goliathPaperCore.service.CommandErrorService;
import ch.mcserver.goliathPaperCore.service.EnderchestService;
import ch.mcserver.goliathPaperCore.service.ShutdownService;
import com.mongodb.client.MongoCollection;
import org.bson.Document;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class PluginRegister {

    private final GoliathPaperCore plugin;

    private final MongoDBManager mongoManager;
    private final MySQLManager mySQLManager;

    private final MongoCollection<Document> enderchestCollection;
    private final PlayerEnderchestRepository enderchestRepository;
    private final EnderchestService enderchestService;

    private final MongoCollection<Document> inventoryCollection;
    private final PlayerInventoryRepository playerInventoryRepository;

    private final ShutdownService shutdownService;

    public PluginRegister(GoliathPaperCore plugin, MongoDBManager mongoManager, MySQLManager mySQLManager) {
        this.plugin = plugin;
        this.mongoManager = mongoManager;
        this.mySQLManager = mySQLManager;

        this.inventoryCollection = this.mongoManager.getMongoCollection("player_inventory");
        this.playerInventoryRepository = new PlayerInventoryRepository(inventoryCollection);

        this.enderchestCollection = this.mongoManager.getMongoCollection("player_enderchest");
        this.enderchestRepository = new PlayerEnderchestRepository(enderchestCollection);
        this.enderchestService = new EnderchestService(enderchestRepository);

        this.shutdownService = new ShutdownService(
                this.plugin.logger, this.mongoManager, this.enderchestService, this.playerInventoryRepository);
    }

    public void registerAll() {
        registerManagers();
        registerCommands();
        registerListeners();
        registerPluginMessaging();
        registerSchedulers();
    }

    public ShutdownService getShutdownService() {
        return shutdownService;
    }

    private void registerSchedulers() {
        Bukkit.getScheduler().runTaskTimer(
                plugin,
                () -> {
                    for (Player player : Bukkit.getOnlinePlayers()) {
                        playerInventoryRepository.saveInventory(player.getUniqueId());
                    }
                },
                20L * 60 * 5,
                20L * 60 * 5
        );
    }

    private void registerListeners() {
        boolean isSpawn = GoliathPaperCore.config
                .node("server", "isSpawn")
                .getBoolean(false);

        if (isSpawn) {
            plugin.getServer().getPluginManager()
                    .registerEvents(new SpawnListener(), plugin);
        }

        plugin.getServer().getPluginManager()
                .registerEvents(new CommandErrorService(), plugin);

        plugin.getServer().getPluginManager()
                .registerEvents(new EnderchestListener(enderchestService, enderchestRepository), plugin);

        plugin.getServer().getPluginManager()
                .registerEvents(new InventoryListener(playerInventoryRepository), plugin);
    }

    private void registerCommands() {
    }

    private void registerManagers() {
    }

    private void registerPluginMessaging() {
        plugin.getServer().getMessenger().registerIncomingPluginChannel(
                plugin,
                "goliath:gtp",
                new GoliathTeleportMessenger()
        );

        plugin.getServer().getMessenger().registerIncomingPluginChannel(
                plugin,
                "goliath:gmsp",
                new GmspMessenger()
        );

        plugin.getServer().getMessenger().registerIncomingPluginChannel(
                plugin,
                "goliath:updatecommands",
                new CommandUpdateMessenger()
        );

        plugin.getServer().getMessenger().registerIncomingPluginChannel(
                plugin,
                "goliath:history",
                new HistorySnapshotMessenger()
        );
    }
}