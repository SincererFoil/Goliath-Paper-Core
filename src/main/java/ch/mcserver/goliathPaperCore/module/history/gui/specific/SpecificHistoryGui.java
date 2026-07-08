package ch.mcserver.goliathPaperCore.module.history.gui.specific;

import ch.mcserver.goliathPaperCore.GoliathPaperCore;
import ch.mcserver.goliathPaperCore.common.database.mongodb.HistoryRepository;
import ch.mcserver.goliathPaperCore.module.history.HistoryEvent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class SpecificHistoryGui {

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss").withZone(ZoneId.of("Europe/Zurich"));

    public static void openSpecificInventory(Player staffPlayer, UUID historyUUID) {

        List<HistoryEvent> specificHistoryEvent = GoliathPaperCore.getHistoryRepository().getEventsByHistoryUUID(historyUUID);

        HistoryEvent event = specificHistoryEvent.get(0);

        String date = formatter.format(Instant.ofEpochMilli(event.createdAt()));

        SpecificHistoryGuiHolder holder = new SpecificHistoryGuiHolder(specificHistoryEvent);

        Inventory specificInventory = Bukkit.createInventory(holder, 27, event.title());

        ItemStack balItem = buildItem(Material.GOLD_INGOT, ChatColor.WHITE + "Bal COMMINGSOON");
        ItemStack enderchestItem = buildItem(Material.ENDER_CHEST, ChatColor.WHITE + "Enderchest");
        ItemStack inventoryItem = buildItem(Material.CHEST, ChatColor.WHITE + "Inventory");
        ItemStack shardItem = buildItem(Material.AMETHYST_SHARD, ChatColor.WHITE + "Shards");
        ItemStack dateItem = buildItem(Material.CLOCK, ChatColor.WHITE + "Date:" + date);
        ItemStack homeItem = buildItem(Material.BLACK_BED, ChatColor.WHITE + "Homes");
        ItemStack rollbackItem = buildItem(Material.FEATHER, ChatColor.WHITE + "ROLLBACK HIS ENTIRE DATA TO THIS POINT - WITH BALANCE");
        ItemStack backItem = buildItem(Material.RED_STAINED_GLASS_PANE, ChatColor.RED + "Back");

        ItemMeta rollbackMeta = rollbackItem.getItemMeta();
        List<String> lore = new ArrayList<>();
        lore.add(ChatColor.DARK_PURPLE + "ONLY OPS CAN DO THIS");
        rollbackMeta.setLore(lore);
        rollbackItem.setItemMeta(rollbackMeta);

        ItemMeta backItemMeta = backItem.getItemMeta();
        List<String> backItemlore = new ArrayList<>();
        backItemlore.add(ChatColor.WHITE + "Click to return");
        backItemMeta.setLore(backItemlore);
        backItem.setItemMeta(backItemMeta);

        specificInventory.setItem(10, balItem);
        specificInventory.setItem(11, enderchestItem);
        specificInventory.setItem(12, inventoryItem);
        specificInventory.setItem(13, shardItem);
        specificInventory.setItem(14, dateItem);
        specificInventory.setItem(15, homeItem);
        specificInventory.setItem(17, rollbackItem);
        specificInventory.setItem(18, backItem);

        holder.setInventory(specificInventory);
        staffPlayer.openInventory(specificInventory);
    }

    private static ItemStack buildItem(Material material, String name) {
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(name);
        item.setItemMeta(meta);
        return item;
    }
}
