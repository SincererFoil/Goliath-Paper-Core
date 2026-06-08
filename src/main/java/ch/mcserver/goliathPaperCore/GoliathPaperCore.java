package ch.mcserver.goliathPaperCore;

import org.bukkit.plugin.java.JavaPlugin;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.yaml.YamlConfigurationLoader;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.logging.Level;
import java.util.logging.Logger;


public final class GoliathPaperCore extends JavaPlugin {

    public static ConfigurationNode config;
    public Logger logger;

    private static GoliathPaperCore instance;


    @Override
    public void onEnable() {
        instance = this;
        logger = getLogger();

        loadConfig();

        new PluginRegister(this).registerAll();


        getLogger().log(Level.INFO, "[Goliath] Plugin Enabled!");

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public static GoliathPaperCore getInstance() {
        return instance;
    }


    private void loadConfig() {
        try {
            Path dataDirectory = getDataFolder().toPath();
            Files.createDirectories(dataDirectory);

            Path configPath = dataDirectory.resolve("config.yml");
            YamlConfigurationLoader loader = YamlConfigurationLoader.builder().path(configPath).build();

            if(!Files.exists(configPath)) {
                config = loader.createNode();
                config.node("mongodb", "uri").set("YOUR_MONGO_CONNECTION_STRING");
                config.node("server", "isSpawn").set(false);
                loader.save(config);
                getLogger().log(Level.INFO, "Config file has been created");
            }
            config = loader.load();

        } catch (Exception e) {
            getLogger().log(Level.SEVERE, "Failed to load config.yml!", e);
            e.printStackTrace();

        }
    }

}
