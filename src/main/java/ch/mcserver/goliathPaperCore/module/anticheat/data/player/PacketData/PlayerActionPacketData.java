package ch.mcserver.goliathPaperCore.module.anticheat.data.player.PacketData;

import com.comphenix.protocol.PacketType;

import java.util.UUID;

public record PlayerActionPacketData(UUID playerUuid, PacketType packetType, PlayerAction action, long receivedAt ) implements PacketData {
}
