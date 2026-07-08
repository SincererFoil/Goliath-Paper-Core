package ch.mcserver.goliathPaperCore.common.packet;

import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

public class ProtocolLibHook {

    private final Plugin plugin;
    private ProtocolManager protocolManager;
    private boolean enabled;

    public ProtocolLibHook(Plugin plugin) {
        this.plugin = plugin;
    }

    public void init() {
        if (Bukkit.getPluginManager().getPlugin("ProtocolLib") == null) {
            plugin.getLogger().warning("[Goliath] ProtocolLib not found. Packet systems disabled");
            this.enabled = false;
            return;
        }

        this.enabled = true;
        this.protocolManager = ProtocolLibrary.getProtocolManager();
        plugin.getLogger().info("[Goliath] ProtocolLib hooked successfully");
    }

    public boolean isEnabled() {
        return enabled;
    }

    public ProtocolManager getProtocolManager() {
        if (!enabled || protocolManager == null) {
            throw new IllegalStateException("ProtocolLib is not available.");
        }

        return protocolManager;
    }
}
