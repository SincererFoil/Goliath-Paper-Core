package ch.mcserver.goliathPaperCore.common.pluginmessage;

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

        boolean gmspEnabled = input.readBoolean();

        Player staff = Bukkit.getPlayer(uuid);

        if (staff == null) {
            return;
        }


        if (gmspEnabled) {
            if (staff.getGameMode() != GameMode.SPECTATOR) {
                staff.setGameMode(GameMode.SPECTATOR);
            }
        } else {
            staff.setSpectatorTarget(null);

            if (staff.getGameMode() != GameMode.SURVIVAL) {
                staff.setGameMode(GameMode.SURVIVAL);
            }
        }
    }
}
