package ch.mcserver.goliathPaperCore.module.history.gui;

import ch.mcserver.goliathPaperCore.GoliathPaperCore;
import ch.mcserver.goliathPaperCore.common.database.mongodb.HistoryRepository;
import ch.mcserver.goliathPaperCore.common.database.mysql.PlayerObject;
import ch.mcserver.goliathPaperCore.module.history.HistoryEvent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ShowHistory implements CommandExecutor {

    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

    public static final int EVENTS_PER_PAGE = 45;
    public static final int SLOT_PREV = 45;
    public static final int SLOT_PAGE_EXAMPLE = 49;
    public static final int SLOT_NEXT = 53;

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String @NotNull [] args) {

        if (args.length != 1) return false;

        if (!(sender instanceof Player player)) {
            sender.sendMessage(ChatColor.RED + "Only players can execute this command.");
            return false;
        }

        String targetName = args[0];

        if (!GoliathPaperCore.getPlayerRepository().existsByUsername(targetName)) {
            player.sendMessage(ChatColor.RED + "Player " + targetName + " does not exist.");
            return false;
        }

        PlayerObject targetObject = GoliathPaperCore.getPlayerRepository().loadPlayerByUsername(targetName);
        UUID targetUUID = targetObject.getUuid();

        openHistory(player, targetUUID);
        return true;
    }

    public static void openHistory(Player staffPlayer, UUID targetUUID) {
        HistoryRepository historyRepository = GoliathPaperCore.getHistoryRepository();
        List<HistoryEvent> events = historyRepository.getEventsByPlayerUUID(targetUUID);
        openPage(staffPlayer, targetUUID, events, 0);
    }

    public static void openPage(Player staffPlayer, UUID targetUUID, List<HistoryEvent> events, int page) {
        HistoryGuiHolder holder = new HistoryGuiHolder(targetUUID, events, page);
        Inventory inventory = Bukkit.createInventory(holder, 54, "History " + page);
        holder.setInventory(inventory);
        renderPage(inventory, holder);
        staffPlayer.openInventory(inventory);
    }

    private static void renderPage(Inventory inventory, HistoryGuiHolder holder) {
        List<HistoryEvent> events = holder.getEvents();
        int page = holder.getPage();

        int fromIndex = page * EVENTS_PER_PAGE;
        int toIndex = Math.min(fromIndex + EVENTS_PER_PAGE, events.size());

        if (fromIndex < toIndex) {
            List<HistoryEvent> pageEvents = events.subList(fromIndex, toIndex);
            for (int i = 0; i < pageEvents.size(); i++) {
                inventory.setItem(i, buildEventItem(pageEvents.get(i)));
            }
        }

        ItemStack item = new ItemStack(Material.NETHER_STAR);

        ItemMeta meta = item.getItemMeta();

        List<String> lore = new ArrayList<>();
        lore.add(ChatColor.DARK_PURPLE + "Example info Button.");
        lore.add(ChatColor.DARK_PURPLE + "I mean it this is just an example");
        meta.setDisplayName(ChatColor.AQUA + "Info Button");
        meta.setLore(lore);
        item.setItemMeta(meta);

        inventory.setItem(SLOT_PREV, buildNavItem(Material.ARROW, ChatColor.GRAY + "Back"));
        inventory.setItem(SLOT_PAGE_EXAMPLE, item);
        inventory.setItem(SLOT_NEXT, buildNavItem(Material.ARROW, ChatColor.GRAY + "Next"));
    }

    private static ItemStack buildEventItem(HistoryEvent event) {
        ItemStack item = new ItemStack(Material.PLAYER_HEAD);
        ItemMeta meta = item.getItemMeta();

        String date = Instant.ofEpochMilli(event.createdAt())
                .atZone(ZoneId.of("Europe/Zurich"))
                .format(DATE_FORMAT);

        meta.setDisplayName(ChatColor.YELLOW + date + " " + event.title());

        List<String> lore = new ArrayList<>();
        lore.add(ChatColor.DARK_PURPLE + "Bal: CommingSoon " + event.historyId());
        lore.add(ChatColor.DARK_PURPLE + event.server());

        meta.setLore(lore);
        item.setItemMeta(meta);
        return item;
    }

    private static ItemStack buildNavItem(Material material, String name) {
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(name);
        item.setItemMeta(meta);
        return item;
    }
}
