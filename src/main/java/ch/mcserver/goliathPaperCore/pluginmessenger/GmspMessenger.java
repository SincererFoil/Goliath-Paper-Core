package ch.mcserver.goliathPaperCore.pluginmessenger;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteStreams;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.PluginMessageListener;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class GmspMessenger implements PluginMessageListener {

    @Override
    public void onPluginMessageReceived(
            @NotNull String channel,
            @NotNull Player receiver,
            byte @NotNull [] message
    ) {

        if (!channel.equalsIgnoreCase("goliath:gmsp")) {
            return;
        }

        ByteArrayDataInput input = ByteStreams.newDataInput(message);

        String subChannel = input.readUTF();

        if (!subChannel.equalsIgnoreCase("GMSP")) {
            return;
        }

        UUID uuid = UUID.fromString(input.readUTF());

        boolean enabled = input.readBoolean();

        Player player = Bukkit.getPlayer(uuid);

        if (player == null) {
            return;
        }

        if (enabled) {

            player.setGameMode(GameMode.SPECTATOR);

        } else {

            player.setGameMode(GameMode.SURVIVAL);
        }
    }
}