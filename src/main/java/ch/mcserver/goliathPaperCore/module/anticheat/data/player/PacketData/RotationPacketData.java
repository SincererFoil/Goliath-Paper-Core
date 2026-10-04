package ch.mcserver.goliathPaperCore.module.anticheat.data.player.PacketData;


import com.github.retrooper.packetevents.protocol.packettype.PacketTypeCommon;

import java.util.UUID;

public record RotationPacketData(UUID playerUuid, PacketTypeCommon packetType, float yaw, float pitch) implements PacketData {
}