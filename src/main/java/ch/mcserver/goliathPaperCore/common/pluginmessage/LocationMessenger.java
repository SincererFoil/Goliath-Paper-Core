package ch.mcserver.goliathPaperCore.common.pluginmessage;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteStreams;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.messaging.PluginMessageListener;

import java.util.UUID;

public class LocationMessenger implements PluginMessageListener {

    private final Plugin plugin;

    public LocationMessenger(Plugin plugin) {
        this.plugin = plugin;

        Bukkit.getMessenger().registerIncomingPluginChannel(
                plugin,
                "goliath:location",
                this
        );
    }

    @Override
    public void onPluginMessageReceived(String channel, Player receiver, byte[] message) {
        if (!channel.equals("goliath:location")) {
            return;
        }

        ByteArrayDataInput input = ByteStreams.newDataInput(message);

        UUID uuid = UUID.fromString(input.readUTF());
        int x = input.readInt();
        int y = input.readInt();
        int z = input.readInt();
        float yaw = input.readFloat();
        float pitch = input.readFloat();

        Bukkit.getScheduler().runTaskLater(plugin, () -> {
            teleportWithRetry(uuid, x, y, z, yaw, pitch, 10);
        }, 20L);
    }

    private void teleportWithRetry(
            UUID uuid,
            int x,
            int y,
            int z,
            float yaw,
            float pitch,
            int triesLeft
    ) {
        if (triesLeft <= 0) {
            return;
        }

        Player target = Bukkit.getPlayer(uuid);

        if (target == null || !target.isOnline()) {
            Bukkit.getScheduler().runTaskLater(plugin, () -> {
                teleportWithRetry(
                        uuid,
                        x,
                        y,
                        z,
                        yaw,
                        pitch,
                        triesLeft - 1
                );
            }, 10L);
            return;
        }

        World world = target.getWorld();

        Location location = new Location(
                world,
                x + 0.5,
                y,
                z + 0.5,
                yaw,
                pitch
        );

        target.teleport(location);
    }
}
