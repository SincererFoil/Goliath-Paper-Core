package ch.mcserver.goliathPaperCore.module.randomteleport;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class OverworldGUI {

    public static void openOverworldGUI(Player player) {

        int NAWestPing = 185;
        int NAEastPing = 146;
        int EUCentralPing = 14;
        int EUWestPing = 18;
        int AsiaPing = 235;
        int OceaniaPing = 327;

        Inventory inventory = Bukkit.createInventory(null, 27, "Random Teleport");

        ItemStack naWest = new ItemStack(Material.GRASS_BLOCK);
        ItemMeta naWestMeta = naWest.getItemMeta();
        naWestMeta.setDisplayName(ChatColor.GREEN + "NA West");

        ItemStack naEast = new ItemStack(Material.GRASS_BLOCK);
        ItemMeta naEastMeta = naEast.getItemMeta();
        naEastMeta.setDisplayName(ChatColor.GREEN + "NA East");

        ItemStack euCentral = new ItemStack(Material.GRASS_BLOCK);
        ItemMeta euCentralMeta = euCentral.getItemMeta();
        euCentralMeta.setDisplayName(ChatColor.GREEN + "EU Central");

        ItemStack euWest = new ItemStack(Material.GRASS_BLOCK);
        ItemMeta euWestMeta = euWest.getItemMeta();
        euWestMeta.setDisplayName(ChatColor.GREEN + "EU West");

        ItemStack asia = new ItemStack(Material.GRASS_BLOCK);
        ItemMeta asiaMeta = asia.getItemMeta();
        asiaMeta.setDisplayName(ChatColor.GREEN + "Asia");

        ItemStack oceania = new ItemStack(Material.GRASS_BLOCK);
        ItemMeta oceaniaMeta = oceania.getItemMeta();
        oceaniaMeta.setDisplayName(ChatColor.GREEN + "Oceania");

        List<String> naWestLore = new ArrayList<>();
        naWestLore.add(ChatColor.WHITE + "Click to randomly teleport");
        naWestLore.add("   ");
        naWestLore.add(ChatColor.GRAY + "Ping (" + ChatColor.AQUA + NAWestPing + "ms" + ChatColor.GRAY + ")");

        List<String> naEastLore = new ArrayList<>();
        naEastLore.add(ChatColor.WHITE + "Click to randomly teleport");
        naEastLore.add("   ");
        naEastLore.add(ChatColor.GRAY + "Ping (" + ChatColor.AQUA + NAEastPing + "ms" + ChatColor.GRAY + ")");

        List<String> euCentralLore = new ArrayList<>();
        euCentralLore.add(ChatColor.WHITE + "Click to randomly teleport");
        euCentralLore.add("   ");
        euCentralLore.add(ChatColor.GRAY + "Ping (" + ChatColor.AQUA + EUCentralPing + "ms" + ChatColor.GRAY + ")");

        List<String> euWestLore = new ArrayList<>();
        euWestLore.add(ChatColor.WHITE + "Click to randomly teleport");
        euWestLore.add("   ");
        euWestLore.add(ChatColor.GRAY + "Ping (" + ChatColor.AQUA + EUWestPing + "ms" + ChatColor.GRAY + ")");

        List<String> asiaLore = new ArrayList<>();
        asiaLore.add(ChatColor.WHITE + "Click to randomly teleport");
        asiaLore.add("   ");
        asiaLore.add(ChatColor.GRAY + "Ping (" + ChatColor.AQUA + AsiaPing + "ms" + ChatColor.GRAY + ")");

        List<String> oceaniaLore = new ArrayList<>();
        oceaniaLore.add(ChatColor.WHITE + "Click to randomly teleport");
        oceaniaLore.add("   ");
        oceaniaLore.add(ChatColor.GRAY + "Ping (" + ChatColor.AQUA + OceaniaPing + "ms" + ChatColor.GRAY + ")");

        naWestMeta.setLore(naWestLore);
        naEastMeta.setLore(naEastLore);
        euCentralMeta.setLore(euCentralLore);
        euWestMeta.setLore(euWestLore);
        asiaMeta.setLore(asiaLore);
        oceaniaMeta.setLore(oceaniaLore);

        naWest.setItemMeta(naWestMeta);
        naEast.setItemMeta(naEastMeta);
        euCentral.setItemMeta(euCentralMeta);
        euWest.setItemMeta(euWestMeta);
        asia.setItemMeta(asiaMeta);
        oceania.setItemMeta(oceaniaMeta);

        inventory.setItem(10, naWest);
        inventory.setItem(11, naEast);
        inventory.setItem(12, euCentral);
        inventory.setItem(13, euWest);
        inventory.setItem(14, asia);
        inventory.setItem(15, oceania);

        player.openInventory(inventory);
    }
}