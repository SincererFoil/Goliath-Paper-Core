package ch.mcserver.goliathPaperCore.module.anticheat.data.player.PacketData;



import com.github.retrooper.packetevents.protocol.packettype.PacketTypeCommon;

import java.util.UUID;

public record DigPacketData(UUID playerUuid, PacketTypeCommon packetType, int blockX, int blockY, int blockZ, DigAction action, long receivedAt) implements PacketData {
}
