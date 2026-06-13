package ch.mcserver.goliathPaperCore;

import ch.mcserver.goliathPaperCore.mongodb.MongoDBManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.yaml.YamlConfigurationLoader;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.logging.Level;
import java.util.logging.Logger;

public final class GoliathPaperCore extends JavaPlugin {

    public static ConfigurationNode config;

    private static GoliathPaperCore instance;

    public Logger logger;
    private MongoDBManager mongoManager;
    private PluginRegister pluginRegister;

    @Override
    public void onEnable() {
        instance = this;
        logger = getLogger();

        loadConfig();

        mongoManager = new MongoDBManager();
        mongoManager.connect();

        pluginRegister = new PluginRegister(this, mongoManager);
        pluginRegister.registerAll();

        logger.info("[Goliath] Plugin Enabled!");
    }

    @Override
    public void onDisable() {
        if (pluginRegister != null && pluginRegister.getShutdownService() != null) {
            pluginRegister.getShutdownService().shutdown();
        }

        logger.info("[Goliath] Plugin Disabled!");
    }

    public static GoliathPaperCore getInstance() {
        return instance;
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
                config.node("server", "isSpawn").set(false);

                loader.save(config);
                logger.info("Config file has been created");
            }

            config = loader.load();

        } catch (Exception e) {
            logger.log(Level.SEVERE, "Failed to load config.yml!", e);
        }
    }
}