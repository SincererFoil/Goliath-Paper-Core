package ch.mcserver.goliathPaperCore.module.history.gui.inventory;

import ch.mcserver.goliathPaperCore.GoliathPaperCore;
import ch.mcserver.goliathPaperCore.module.history.HistoryEvent;
import ch.mcserver.goliathPaperCore.module.history.snapshot.PlayerInventorySnapshotRepository;
import org.bson.Document;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static ch.mcserver.goliathPaperCore.module.enderchest.PlayerEnderchestRepository.fromBase64;

public class HistoryInventoryGui {

    public static void openHistoryInventory(Player staffPlayer, List<HistoryEvent> events) {

        HistoryEvent historyEvent = events.getFirst();
        InventoryHolder holder = new SpecificHistoryInventoryGuiHolder(events);

        Inventory historyInventory = Bukkit.createInventory(holder, 45, "Contents");

        PlayerInventorySnapshotRepository playerInventorySnapshotRepository = GoliathPaperCore.getPlayerInventorySnapshotRepository();

        Document document = playerInventorySnapshotRepository.getInventoryByHistoryUUID(UUID.fromString(historyEvent.historyId()));

        if (document == null) {
            staffPlayer.sendMessage(ChatColor.RED + "No inventory found for " + UUID.fromString(historyEvent.historyId()));
            return;
        }

        ItemStack[] contents = fromBase64(document.getString("inventory"));
        ItemStack[] armor = fromBase64(document.getString("armor"));
        ItemStack[] offhand = fromBase64(document.getString("offhand"));
        ItemStack returnPane = new ItemStack(Material.RED_STAINED_GLASS_PANE);

        for (int i = 0; i < contents.length && i < 36; i++) {
            historyInventory.setItem(i, contents[i]);
        }

        if (offhand.length > 0) {
            historyInventory.setItem(40, offhand[0]);
        }

        for (int i = 0; i < armor.length && i < 4; i++) {
            historyInventory.setItem(41 + i, armor[i]);
        }

        ItemMeta meta = returnPane.getItemMeta();
        List<String> lore = new ArrayList<>();
        meta.setDisplayName(ChatColor.RED + "Back");
        lore.add(ChatColor.WHITE + "Click to return");
        meta.setLore(lore);
        returnPane.setItemMeta(meta);

        historyInventory.setItem(36, returnPane);

        staffPlayer.openInventory(historyInventory);
    }
}
