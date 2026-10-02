package ch.mcserver.goliathPaperCore.module.anticheat.data.player.PacketData;

import com.comphenix.protocol.PacketType;

import java.util.UUID;

public record WindowPacketData(UUID playerUuid, PacketType packetType, String windowTitle, boolean open) implements PacketData{
}
