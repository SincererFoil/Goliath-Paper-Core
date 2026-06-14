package ch.mcserver.goliathPaperCore.common.pluginmessage;

import ch.mcserver.goliathPaperCore.GoliathPaperCore;
import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteStreams;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.PluginMessageListener;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class CommandUpdateMessenger implements PluginMessageListener {

    @Override
    public void onPluginMessageReceived(
            @NotNull String channel,
            @NotNull Player source,
            @NotNull byte[] message
    ) {

        if (!channel.equalsIgnoreCase("goliath:updatecommands")) {
            return;
        }

        ByteArrayDataInput input = ByteStreams.newDataInput(message);
        UUID uuid = UUID.fromString(input.readUTF());

        Player player = Bukkit.getPlayer(uuid);

        if (player == null) {
            return;
        }

        Bukkit.getScheduler().runTask(GoliathPaperCore.getInstance(), player::updateCommands);
    }
}