package ch.mcserver.goliathPaperCore.module.anticheat.data.player.PacketData;

import com.github.retrooper.packetevents.protocol.packettype.PacketTypeCommon;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;

public record InventoryClickPacketData(UUID playerUuid, PacketTypeCommon packetType, int windowId, int slot, int button, ClickType clickType, ItemStack cursorItem, long receivedAt) implements PacketData {
}
