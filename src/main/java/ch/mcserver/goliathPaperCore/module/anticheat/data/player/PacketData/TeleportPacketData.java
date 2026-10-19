package ch.mcserver.goliathPaperCore.module.anticheat.data.player.PacketData;


import com.github.retrooper.packetevents.protocol.packettype.PacketTypeCommon;

import java.util.UUID;

public record TeleportPacketData(UUID playerUuid, PacketTypeCommon packetType, double x, double y, double z, int teleportId, long receivedAt) implements PacketData {
}