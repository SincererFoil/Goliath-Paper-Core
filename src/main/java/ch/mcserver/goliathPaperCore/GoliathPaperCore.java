package ch.mcserver.goliathPaperCore;

import ch.mcserver.goliathPaperCore.common.database.mongodb.HistoryRepository;
import ch.mcserver.goliathPaperCore.common.database.mongodb.MongoDBManager;
import ch.mcserver.goliathPaperCore.common.database.mysql.MySQLManager;
import ch.mcserver.goliathPaperCore.common.database.mysql.PlayerRepository;
import org.bukkit.plugin.java.JavaPlugin;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.yaml.YamlConfigurationLoader;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.logging.Level;
import java.util.logging.Logger;

public final class GoliathPaperCore extends JavaPlugin {

    public static ConfigurationNode config;
    public static PlayerRepository playerRepository;
    public static HistoryRepository historyRepository;

    private static GoliathPaperCore instance;

    public Logger logger;

    private MongoDBManager mongoManager;
    private MySQLManager mySQLManager;
    private PluginRegister pluginRegister;

    @Override
    public void onEnable() {
        instance = this;
        logger = getLogger();

        loadConfig();

        mongoManager = new MongoDBManager();
        mongoManager.connect();

        mySQLManager = new MySQLManager();
        mySQLManager.connect();

        playerRepository = new PlayerRepository(mySQLManager);

        pluginRegister = new PluginRegister(this, mongoManager, mySQLManager);
        pluginRegister.registerAll();

        logger.info("[Goliath] Plugin Enabled!");
    }

    @Override
    public void onDisable() {
        if (pluginRegister != null && pluginRegister.getShutdownService() != null) {
            pluginRegister.getShutdownService().shutdown();
        }

        if (mongoManager != null) {
            mongoManager.disconnect();
        }

        if (mySQLManager != null) {
            mySQLManager.disconnect();
        }

        logger.info("[Goliath] Plugin Disabled!");
    }

    public static GoliathPaperCore getInstance() {
        return instance;
    }

    public static PlayerRepository getPlayerRepository() {
        return playerRepository;
    }

    public static HistoryRepository getHistoryRepository() {
        return historyRepository;
    }

    private void loadConfig() {
        try {
            Path dataDirectory = getDataFolder().toPath();
            Files.createDirectories(dataDirectory);

            Path configPath = dataDirectory.resolve("config.yml");
            YamlConfigurationLoader loader = YamlConfigurationLoader.builder()
                    .path(configPath)
                    .build();

            if (!Files.exists(configPath)) {
                config = loader.createNode();

                config.node("mongodb", "uri").set("YOUR_MONGO_CONNECTION_STRING");
                config.node("mysql", "host").set("127.0.0.1");
                config.node("mysql", "port").set(3306);
                config.node("mysql", "database").set("goliath");
                config.node("mysql", "username").set("goliath");
                config.node("mysql", "password").set("");

                config.node("server", "isSpawn").set(false);
                config.node("server", "name").set("goliath-unknown");

                loader.save(config);
                logger.info("Config file has been created");
            }

            config = loader.load();

        } catch (Exception e) {
            logger.log(Level.SEVERE, "Failed to load config.yml!", e);
        }
    }
}