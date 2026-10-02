package ch.mcserver.goliathPaperCore.module.anticheat.data.player.PacketData;

import com.comphenix.protocol.PacketType;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;

public record BlockPlacePacketData(UUID playerUuid, PacketType packetType, int blockX, int blockY, int blockZ, int face, ItemStack itemInHand, long receivedAt) implements PacketData {
}
