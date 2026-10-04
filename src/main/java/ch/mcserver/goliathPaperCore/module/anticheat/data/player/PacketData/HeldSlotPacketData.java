package ch.mcserver.goliathPaperCore.module.anticheat.data.player.PacketData;



import com.github.retrooper.packetevents.protocol.packettype.PacketTypeCommon;

import java.util.UUID;

public record HeldSlotPacketData(UUID playerUuid, PacketTypeCommon packetType, int slot, long receivedAt) implements PacketData {
}