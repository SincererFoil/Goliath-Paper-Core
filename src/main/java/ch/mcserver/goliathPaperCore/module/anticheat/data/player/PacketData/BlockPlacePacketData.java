package ch.mcserver.goliathPaperCore.module.anticheat.data.player.PacketData;

import com.github.retrooper.packetevents.protocol.packettype.PacketTypeCommon;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;

public record BlockPlacePacketData(UUID playerUuid, PacketTypeCommon packetType, int blockX, int blockY, int blockZ, int face, ItemStack itemInHand, long receivedAt) implements PacketData {
}
