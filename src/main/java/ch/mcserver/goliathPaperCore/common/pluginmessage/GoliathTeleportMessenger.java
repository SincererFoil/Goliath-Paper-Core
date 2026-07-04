package ch.mcserver.goliathPaperCore.common.pluginmessage;

import ch.mcserver.goliathPaperCore.common.database.mysql.*;
import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteStreams;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.PluginMessageListener;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class GoliathTeleportMessenger implements PluginMessageListener {

    private final PlayerLocationRepository playerLocationRepository;

    public GoliathTeleportMessenger(PlayerLocationRepository playerLocationRepository) {
        this.playerLocationRepository = playerLocationRepository;
    }

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

        if (staff == null) {
            return;
        }
        if (target == null || !target.isOnline()) {
            PlayerLocationObject targetLocation = playerLocationRepository.loadPlayer(targetUuid);
            if (targetLocation == null) {
                return;
            }

            staff.teleport(new Location(
                    staff.getWorld(),
                    targetLocation.getX() + 0.5,
                    targetLocation.getY(),
                    targetLocation.getZ() + 0.5,
                    targetLocation.getYaw(),
                    targetLocation.getPitch()
            ));
        } else {
            staff.teleport(target.getLocation());
        }

        staff.playSound(staff.getLocation(), Sound.ENTITY_PLAYER_TELEPORT, 1, 1);

    }
}