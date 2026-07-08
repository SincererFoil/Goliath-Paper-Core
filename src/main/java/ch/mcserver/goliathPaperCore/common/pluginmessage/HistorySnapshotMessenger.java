package ch.mcserver.goliathPaperCore.common.pluginmessage;

import ch.mcserver.goliathPaperCore.GoliathPaperCore;
import ch.mcserver.goliathPaperCore.module.history.snapshot.HistorySnapshot;
import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteStreams;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.PluginMessageListener;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;
import java.util.logging.Level;

public class HistorySnapshotMessenger implements PluginMessageListener {

    @Override
    public void onPluginMessageReceived(
            @NotNull String channel,
            @NotNull Player source,
            @NotNull byte[] message
    ) {

        if (!channel.equalsIgnoreCase("goliath:history")) {
            return;
        }

        ByteArrayDataInput input = ByteStreams.newDataInput(message);

        String subChannel = input.readUTF();

        if (!subChannel.equalsIgnoreCase("HISTORY")) {
            return;
        }

        UUID uuid = UUID.fromString(input.readUTF());
        String historyId = input.readUTF();
        String type = input.readUTF();
        UUID historyUUID = UUID.fromString(historyId);
        Player player = Bukkit.getPlayer(uuid);

        if (player == null) {
            return;
        }
        HistorySnapshot.createSnapshot(uuid, historyUUID, type);
        GoliathPaperCore.getInstance().logger.log(Level.FINE, "[Goliath] Snapshot is not finished yet!");


    }
}
