package ch.mcserver.goliathPaperCore.module.anticheat.data.player.PacketData;


import com.github.retrooper.packetevents.protocol.packettype.PacketTypeCommon;

import java.util.UUID;

public record MovementPacketData(UUID playerUuid, PacketTypeCommon packetType, double x, double y, double z, boolean onGround, boolean hasPosition) implements PacketData {
}