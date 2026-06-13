package ch.mcserver.goliathPaperCore;

import ch.mcserver.goliathPaperCore.listener.EnderchestListener;
import ch.mcserver.goliathPaperCore.listener.SpawnListener;
import ch.mcserver.goliathPaperCore.mongodb.MongoDBManager;
import ch.mcserver.goliathPaperCore.mongodb.repository.PlayerEnderchestRepository;
import ch.mcserver.goliathPaperCore.pluginmessenger.CommandUpdateMessenger;
import ch.mcserver.goliathPaperCore.pluginmessenger.GmspMessenger;
import ch.mcserver.goliathPaperCore.pluginmessenger.GoliathTeleportMessenger;
import ch.mcserver.goliathPaperCore.pluginmessenger.HistorySnapshotMessenger;
import ch.mcserver.goliathPaperCore.service.CommandErrorService;
import ch.mcserver.goliathPaperCore.service.EnderchestService;
import com.mongodb.client.MongoCollection;
import org.bson.Document;

public class PluginRegister {

    private final GoliathPaperCore plugin;
    private final MongoDBManager mongoManager;
    private final MongoCollection<Document> enderchestCollection;
    private final PlayerEnderchestRepository enderchestRepository;
    private final EnderchestService enderchestService;

    public PluginRegister(GoliathPaperCore plugin, MongoDBManager mongoManager) {
        this.plugin = plugin;
        this.mongoManager = mongoManager;

        this.enderchestCollection = this.mongoManager.getMongoCollection("player_enderchest");
        this.enderchestRepository = new PlayerEnderchestRepository(enderchestCollection);
        this.enderchestService = new EnderchestService(enderchestRepository);

        plugin.setEnderchestService(enderchestService);
    }

    public void registerAll() {
        registerManagers();
        registerCommands();
        registerListeners();
        registerPluginMessaging();
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