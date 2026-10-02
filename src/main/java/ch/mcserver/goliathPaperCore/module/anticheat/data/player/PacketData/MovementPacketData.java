package ch.mcserver.goliathPaperCore.module.anticheat.data.player.PacketData;

import com.comphenix.protocol.PacketType;
import java.util.UUID;

public record MovementPacketData(UUID playerUuid, PacketType packetType, double x, double y, double z, boolean onGround, boolean hasPosition) implements PacketData {
}