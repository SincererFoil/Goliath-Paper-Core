package ch.mcserver.goliathPaperCore.module.anticheat.data.player.PacketData;



import com.github.retrooper.packetevents.protocol.packettype.PacketTypeCommon;

import java.util.UUID;

public record PlayerActionPacketData(UUID playerUuid, PacketTypeCommon packetType, PlayerAction action, long receivedAt ) implements PacketData {
}
