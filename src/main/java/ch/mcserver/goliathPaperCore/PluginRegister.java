package ch.mcserver.goliathPaperCore;

import ch.mcserver.goliathPaperCore.listener.EnderchestListener;
import ch.mcserver.goliathPaperCore.listener.SpawnListener;
import ch.mcserver.goliathPaperCore.pluginmessenger.CommandUpdateMessenger;
import ch.mcserver.goliathPaperCore.pluginmessenger.GmspMessenger;
import ch.mcserver.goliathPaperCore.pluginmessenger.GoliathTeleportMessenger;
import ch.mcserver.goliathPaperCore.pluginmessenger.HistorySnapshotMessenger;
import ch.mcserver.goliathPaperCore.service.CommandErrorService;
import ch.mcserver.goliathPaperCore.service.EnderchestService;

import static org.bukkit.Bukkit.getServer;

public class PluginRegister {

    private final GoliathPaperCore plugin;

    public PluginRegister(GoliathPaperCore plugin) {
        this.plugin = plugin;
    }

    public void registerAll() {
        registerManagers();
        registerCommands();
        registerListeners();
        registerPluginMessaging();
    }

    private void registerListeners() {
        boolean isSpawn = GoliathPaperCore.config.node("server", "isSpawn").getBoolean(false);
        EnderchestService enderchestService = new EnderchestService();

        if (isSpawn) {
            getServer().getPluginManager().registerEvents(new SpawnListener(), plugin);
        }
        getServer().getPluginManager().registerEvents(new CommandErrorService(), plugin);
        getServer().getPluginManager().registerEvents(new EnderchestListener(enderchestService), plugin);

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
