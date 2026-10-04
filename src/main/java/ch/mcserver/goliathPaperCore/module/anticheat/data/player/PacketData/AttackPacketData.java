package ch.mcserver.goliathPaperCore.module.anticheat.data.player.PacketData;




import com.github.retrooper.packetevents.protocol.packettype.PacketTypeCommon;

import java.util.UUID;

public record AttackPacketData(UUID playerUuid, PacketTypeCommon packetType, int targetEntityId, String targetName, long receivedAt) implements PacketData {
}
