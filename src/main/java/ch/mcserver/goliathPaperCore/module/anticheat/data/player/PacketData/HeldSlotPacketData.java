package ch.mcserver.goliathPaperCore.module.anticheat.data.player.PacketData;

import com.comphenix.protocol.PacketType;

import java.util.UUID;

public record HeldSlotPacketData(UUID playerUuid, PacketType packetType, int slot, long receivedAt) implements PacketData {
}