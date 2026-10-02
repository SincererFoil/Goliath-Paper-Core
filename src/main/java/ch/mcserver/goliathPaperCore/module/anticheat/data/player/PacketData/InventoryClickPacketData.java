package ch.mcserver.goliathPaperCore.module.anticheat.data.player.PacketData;

import com.comphenix.protocol.PacketType;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;

public record InventoryClickPacketData(UUID playerUuid, PacketType packetType, int windowId, int slot, int button, ClickType clickType, ItemStack cursorItem, long receivedAt) implements PacketData {
}
