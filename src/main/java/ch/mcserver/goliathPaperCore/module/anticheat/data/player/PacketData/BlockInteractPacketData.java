package ch.mcserver.goliathPaperCore.module.anticheat.data.player.PacketData;



import com.github.retrooper.packetevents.protocol.packettype.PacketTypeCommon;

import java.util.UUID;

public record BlockInteractPacketData(UUID playerUuid, PacketTypeCommon packetType, int blockX, int blockY, int blockZ, int face, long receivedAt) implements PacketData {
}
