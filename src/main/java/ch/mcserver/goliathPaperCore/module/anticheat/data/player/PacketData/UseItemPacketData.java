package ch.mcserver.goliathPaperCore.module.anticheat.data.player.PacketData;

import com.github.retrooper.packetevents.protocol.packettype.PacketTypeCommon;
import com.github.retrooper.packetevents.protocol.player.InteractionHand;

import java.util.UUID;

public record UseItemPacketData(UUID playerUuid, PacketTypeCommon packetType, InteractionHand hand, long receivedAt) implements PacketData {
}
