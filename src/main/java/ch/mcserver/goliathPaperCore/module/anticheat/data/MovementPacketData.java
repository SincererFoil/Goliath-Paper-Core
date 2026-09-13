package ch.mcserver.goliathPaperCore.module.anticheat.data;

import com.comphenix.protocol.PacketType;
import java.util.UUID;

public record MovementPacketData(UUID playerUuid, PacketType packetType, float yaw, float pitch, double x, double y, double z, boolean hasPosition, boolean hasRotation) implements PacketData {
}