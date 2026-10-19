package ch.mcserver.goliathPaperCore.module.anticheat.checks;

import ch.mcserver.goliathPaperCore.module.anticheat.data.player.PacketData.PacketData;
import ch.mcserver.goliathPaperCore.module.anticheat.data.player.playerdata.PlayerData;
import ch.mcserver.goliathPaperCore.module.anticheat.flag.FlagManager;
import ch.mcserver.goliathPaperCore.module.anticheat.flag.FlagType;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.protocol.packettype.PacketTypeCommon;

import java.util.Set;

public class InvalidA extends Check{

    public InvalidA(PlayerData playerData, FlagManager flagManager) {
        super(playerData, FlagType.INVALID_A, flagManager);
    }

    @Override
    public void handle(PacketData data) {
        float pitch = playerData.getPitch();
        if (pitch < -90 || pitch > 90) {
            buffer += 0.5;

            if (buffer >= 10) {
                flag(String.valueOf(buffer));
                buffer -= 10;
            }

        } else  {
            buffer = Math.max(0, buffer - 0.15);
        }

    }

    @Override
    public Set<PacketTypeCommon> getPacketTypes() {
        return Set.of(PacketType.Play.Client.PLAYER_ROTATION, PacketType.Play.Client.PLAYER_POSITION_AND_ROTATION);
    }
}