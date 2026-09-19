package ch.mcserver.goliathPaperCore.module.anticheat.checks;

import ch.mcserver.goliathPaperCore.module.anticheat.data.PacketData;
import ch.mcserver.goliathPaperCore.module.anticheat.data.PlayerData;
import ch.mcserver.goliathPaperCore.module.anticheat.flag.FlagManager;
import ch.mcserver.goliathPaperCore.module.anticheat.flag.FlagType;
import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketEvent;

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
    public Set<PacketType> getPacketTypes() {
        return Set.of(PacketType.Play.Client.LOOK, PacketType.Play.Client.POSITION_LOOK);
    }
}