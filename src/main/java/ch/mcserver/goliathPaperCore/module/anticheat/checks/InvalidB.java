package ch.mcserver.goliathPaperCore.module.anticheat.checks;

import ch.mcserver.goliathPaperCore.module.anticheat.data.player.PacketData.PacketData;
import ch.mcserver.goliathPaperCore.module.anticheat.data.player.playerdata.PlayerData;
import ch.mcserver.goliathPaperCore.module.anticheat.flag.FlagManager;
import ch.mcserver.goliathPaperCore.module.anticheat.flag.FlagType;
import com.comphenix.protocol.PacketType;

import java.util.Set;

public class InvalidB extends Check{

    public InvalidB(PlayerData playerData, FlagManager flagManager) {
        super(playerData, FlagType.INVALID_B, flagManager);
    }

    @Override
    public void handle(PacketData event) {
    }

    @Override
    public Set<PacketType> getPacketTypes() {
        return Set.of();
    }
}
