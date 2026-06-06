package ch.mcserver.goliathPaperCore;

import ch.mcserver.goliathPaperCore.GoliathPaperCore;
import ch.mcserver.goliathPaperCore.pluginmessenger.GmspMessenger;
import ch.mcserver.goliathPaperCore.pluginmessenger.GoliathTeleportMessenger;

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
    }
}
