package ch.mcserver.goliathPaperCore.module.anticheat.data.player.PacketData;



import com.github.retrooper.packetevents.protocol.packettype.PacketTypeCommon;
import net.kyori.adventure.text.Component;

import java.util.UUID;

public record WindowPacketData(UUID playerUuid, PacketTypeCommon packetType, Component windowTitle, int windowId, boolean open, long receivedAt) implements PacketData{
}
