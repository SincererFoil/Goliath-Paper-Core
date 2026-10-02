package ch.mcserver.goliathPaperCore.module.anticheat.data.player.PacketData;

import com.comphenix.protocol.PacketType;

import java.util.UUID;

public record VelocityPacketData(UUID playerUuid, PacketType packetType, double velocityX, double velocityY, double velocityZ, long receivedAt) implements PacketData {
}