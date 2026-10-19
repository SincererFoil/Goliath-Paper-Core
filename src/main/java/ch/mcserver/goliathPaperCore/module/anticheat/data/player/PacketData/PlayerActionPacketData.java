package ch.mcserver.goliathPaperCore.module.anticheat.data.player.PacketData;



import com.github.retrooper.packetevents.protocol.packettype.PacketTypeCommon;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientEntityAction;
import org.bukkit.event.block.Action;

import java.util.UUID;

public record PlayerActionPacketData(UUID playerUuid, PacketTypeCommon packetType, WrapperPlayClientEntityAction.Action action, long receivedAt ) implements PacketData {
}
