package ch.mcserver.goliathPaperCore.module.anticheat.data.player.PacketData;



import com.github.retrooper.packetevents.protocol.packettype.PacketTypeCommon;

import java.util.UUID;

public record WindowPacketData(UUID playerUuid, PacketTypeCommon packetType, String windowTitle, boolean open) implements PacketData{
}
