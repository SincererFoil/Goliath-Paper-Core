package ch.mcserver.goliathPaperCore.module.anticheat.data.player.PacketData;


import com.github.retrooper.packetevents.protocol.packettype.PacketTypeCommon;

import java.util.UUID;

public record VelocityPacketData(UUID playerUuid, PacketTypeCommon packetType, double velocityX, double velocityY, double velocityZ, long receivedAt) implements PacketData {
}