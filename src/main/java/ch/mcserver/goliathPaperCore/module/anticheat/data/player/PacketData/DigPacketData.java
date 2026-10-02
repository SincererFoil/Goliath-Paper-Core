package ch.mcserver.goliathPaperCore.module.anticheat.data.player.PacketData;

import com.comphenix.protocol.PacketType;

import java.util.UUID;

public record DigPacketData(UUID playerUuid, PacketType packetType, int blockX, int blockY, int blockZ, DigAction action, long receivedAt) implements PacketData {
}
