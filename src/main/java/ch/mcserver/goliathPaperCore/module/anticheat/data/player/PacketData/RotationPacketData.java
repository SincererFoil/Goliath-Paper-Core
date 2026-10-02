package ch.mcserver.goliathPaperCore.module.anticheat.data.player.PacketData;

import com.comphenix.protocol.PacketType;

import java.util.UUID;

public record RotationPacketData(UUID playerUuid, PacketType packetType, float yaw, float pitch) implements PacketData {
}