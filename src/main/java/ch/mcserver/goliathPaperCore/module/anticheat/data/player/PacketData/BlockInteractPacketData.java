package ch.mcserver.goliathPaperCore.module.anticheat.data.player.PacketData;

import com.comphenix.protocol.PacketType;

import java.util.UUID;

public record BlockInteractPacketData(UUID playerUuid, PacketType packetType, int blockX, int blockY, int blockZ, int face, long receivedAt) implements PacketData {
}
