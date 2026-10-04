package ch.mcserver.goliathPaperCore.module.anticheat.data.player.PacketData;

import com.github.retrooper.packetevents.protocol.item.ItemStack;
import com.github.retrooper.packetevents.protocol.packettype.PacketTypeCommon;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientClickWindow;

import java.util.UUID;

public record InventoryClickPacketData(UUID playerUuid, PacketTypeCommon packetType, int windowId, int slot, int button, WrapperPlayClientClickWindow.WindowClickType clickType, ItemStack cursorItem, long receivedAt) implements PacketData {
}
