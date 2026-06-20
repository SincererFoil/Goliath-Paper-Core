package ch.mcserver.goliathPaperCore;

import ch.mcserver.goliathPaperCore.common.database.mongodb.MongoDBManager;
import ch.mcserver.goliathPaperCore.common.database.mysql.MySQLManager;
import ch.mcserver.goliathPaperCore.common.database.mysql.PlayerLocationManager;
import ch.mcserver.goliathPaperCore.common.database.mysql.PlayerLocationRepository;
import ch.mcserver.goliathPaperCore.common.packet.GoliathPacket;
import ch.mcserver.goliathPaperCore.common.packet.ProtocolLibHook;
import ch.mcserver.goliathPaperCore.common.pluginmessage.CommandUpdateMessenger;
import ch.mcserver.goliathPaperCore.common.pluginmessage.GmspMessenger;
import ch.mcserver.goliathPaperCore.common.pluginmessage.GoliathTeleportMessenger;
import ch.mcserver.goliathPaperCore.common.pluginmessage.HistorySnapshotMessenger;
import ch.mcserver.goliathPaperCore.common.pluginmessage.LocationPluginMessageListener;
import ch.mcserver.goliathPaperCore.common.service.CommandErrorService;
import ch.mcserver.goliathPaperCore.common.service.ShutdownService;
import ch.mcserver.goliathPaperCore.common.service.SpawnerService;
import ch.mcserver.goliathPaperCore.module.spawn.DoubleJumpBoostListener;
import ch.mcserver.goliathPaperCore.module.enderchest.EnderchestListener;
import ch.mcserver.goliathPaperCore.module.enderchest.EnderchestService;
import ch.mcserver.goliathPaperCore.module.enderchest.PlayerEnderchestRepository;
import ch.mcserver.goliathPaperCore.module.inventory.InventoryListener;
import ch.mcserver.goliathPaperCore.module.inventory.PlayerInventoryRepository;
import ch.mcserver.goliathPaperCore.module.spawn.SpawnListener;
import ch.mcserver.goliathPaperCore.module.spawnstash.SpawnStashCommand;
import ch.mcserver.goliathPaperCore.module.spawnstash.SpawnStashTabCompleter;
import com.mongodb.client.MongoCollection;
import org.bson.Document;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class PluginRegister {

    private final GoliathPaperCore plugin;
    private final MongoDBManager mongoManager;
    private final MySQLManager mySQLManager;

    private MongoCollection<Document> enderchestCollection;
    private PlayerEnderchestRepository enderchestRepository;
    private EnderchestService enderchestService;

    private MongoCollection<Document> inventoryCollection;
    private PlayerInventoryRepository playerInventoryRepository;

    private PlayerLocationRepository playerLocationRepository;
    private PlayerLocationManager playerLocationManager;

    private ShutdownService shutdownService;

    private ProtocolLibHook protocolLibHook;
    private GoliathPacket goliathPacket;

    public PluginRegister(GoliathPaperCore plugin, MongoDBManager mongoManager, MySQLManager mySQLManager) {
        this.plugin = plugin;
        this.mongoManager = mongoManager;
        this.mySQLManager = mySQLManager;
    }

    public void registerAll() {
        registerManagers();
        registerPacketSystems();
        registerCommands();
        registerListeners();
        registerPluginMessaging();
        registerSchedulers();
    }

    public ShutdownService getShutdownService() {
        return shutdownService;
    }

    public ProtocolLibHook getProtocolLibHook() {
        return protocolLibHook;
    }

    public GoliathPacket getGoliathPacket() {
        return goliathPacket;
    }

    private void registerManagers() {
        this.inventoryCollection = this.mongoManager.getMongoCollection("player_inventory");
        this.playerInventoryRepository = new PlayerInventoryRepository(inventoryCollection);

        this.enderchestCollection = this.mongoManager.getMongoCollection("player_enderchest");
        this.enderchestRepository = new PlayerEnderchestRepository(enderchestCollection);
        this.enderchestService = new EnderchestService(enderchestRepository);

        this.playerLocationRepository = new PlayerLocationRepository(mySQLManager);

        String serverName = GoliathPaperCore.config
                .node("server", "name")
                .getString("unknown");

        this.playerLocationManager = new PlayerLocationManager(
                plugin,
                playerLocationRepository,
                serverName
        );

        this.shutdownService = new ShutdownService(
                this.plugin.logger,
                this.mongoManager,
                this.enderchestService,
                this.playerInventoryRepository
        );
    }

    private void registerPacketSystems() {
        this.protocolLibHook = new ProtocolLibHook(plugin);
        this.protocolLibHook.init();

        this.goliathPacket = new GoliathPacket(protocolLibHook);

        if (!this.goliathPacket.isEnabled()) {
            plugin.getLogger().warning("[Goliath] Packet systems disabled because ProtocolLib is missing.");
            return;
        }

        plugin.getLogger().info("[Goliath] Packet systems enabled.");
    }

    private void registerCommands() {
        plugin.getCommand("spawnstash").setExecutor(new SpawnStashCommand());
        plugin.getCommand("spawnstash").setTabCompleter(new SpawnStashTabCompleter());
    }

    private void registerListeners() {
        boolean isSpawn = GoliathPaperCore.config
                .node("server", "isSpawn")
                .getBoolean(false);

        if (isSpawn) {
            plugin.getServer().getPluginManager()
                    .registerEvents(new SpawnListener(), plugin);

            plugin.getServer().getPluginManager()
                    .registerEvents(new DoubleJumpBoostListener(plugin), plugin);
        }

        plugin.getServer().getPluginManager()
                .registerEvents(new CommandErrorService(), plugin);

        plugin.getServer().getPluginManager()
                .registerEvents(new EnderchestListener(enderchestService, enderchestRepository), plugin);

        plugin.getServer().getPluginManager()
                .registerEvents(new InventoryListener(playerInventoryRepository), plugin);

        plugin.getServer().getPluginManager()
                .registerEvents(new SpawnerService(), plugin);

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

        new LocationPluginMessageListener(plugin);
    }

    private void registerSchedulers() {
        Bukkit.getScheduler().runTaskTimer(
                plugin,
                () -> {
                    if (playerInventoryRepository == null) {
                        return;
                    }

                    for (Player player : Bukkit.getOnlinePlayers()) {
                        playerInventoryRepository.saveInventory(player.getUniqueId());
                    }
                },
                20L * 60 * 5,
                20L * 60 * 5
        );
    }
}