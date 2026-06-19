package ch.mcserver.goliathPaperCore.common.pluginmessage;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteStreams;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.PluginMessageListener;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class GoliathTeleportMessenger implements PluginMessageListener {

    @Override
    public void onPluginMessageReceived(
            @NotNull String channel,
            @NotNull Player receiver,
            byte @NotNull [] message
    ) {

        if (!channel.equalsIgnoreCase("goliath:gtp")) {
            return;
        }

        ByteArrayDataInput input = ByteStreams.newDataInput(message);

        String subChannel = input.readUTF();

        if (!subChannel.equalsIgnoreCase("GTP")) {
            return;
        }

        UUID staffUuid = UUID.fromString(input.readUTF());
        UUID targetUuid = UUID.fromString(input.readUTF());
        boolean gmspEnabled = input.readBoolean();

        Player staff = Bukkit.getPlayer(staffUuid);
        Player target = Bukkit.getPlayer(targetUuid);

        if (staff == null || target == null) {
            return;
        }


        staff.setGameMode(GameMode.SPECTATOR);
        staff.teleport(target.getLocation());
        staff.playSound(staff.getLocation(), Sound.ENTITY_PLAYER_TELEPORT, 1, 1);


        if (!gmspEnabled) {
            staff.setGameMode(GameMode.SURVIVAL);
        }  else  {
            staff.setGameMode(GameMode.SPECTATOR);
        }
    }
}