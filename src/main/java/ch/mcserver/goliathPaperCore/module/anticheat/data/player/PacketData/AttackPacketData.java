package ch.mcserver.goliathPaperCore.module.anticheat.data.player.PacketData;

import com.comphenix.protocol.PacketType;

import java.util.UUID;

public record AttackPacketData(UUID playerUuid, PacketType packetType, int targetEntityId, String targetName, long receivedAt) implements PacketData {
}
