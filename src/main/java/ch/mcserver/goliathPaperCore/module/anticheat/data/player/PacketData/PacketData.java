package ch.mcserver.goliathPaperCore.module.anticheat.data.player.PacketData;

import com.github.retrooper.packetevents.protocol.packettype.PacketTypeCommon;

import java.util.UUID;

public interface PacketData {
    UUID playerUuid();
    PacketTypeCommon packetType();
}