package ch.mcserver.goliathPaperCore.module.randomteleport;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerItemMendEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;


public class TeleportGUI {
    public static void openGUI(Player player) {

        int overworldPlayerCount = 21123;
        int netherPlayerCount = 12321;
        int endPlayerCount = 1853;

        Inventory inventory = Bukkit.createInventory(null, 27, "Random Teleport");


        ItemStack overworldItem = new ItemStack(Material.GRASS_BLOCK);
        ItemMeta overworldItemMeta = overworldItem.getItemMeta();
        overworldItemMeta.setDisplayName(ChatColor.WHITE + "Overworld");

        ItemStack netherItem = new ItemStack(Material.NETHERRACK);
        ItemMeta netherItemMeta = netherItem.getItemMeta();
        netherItemMeta.setDisplayName("Nether");

        ItemStack endItem = new ItemStack(Material.END_STONE);
        ItemMeta endItemMeta = endItem.getItemMeta();
        endItemMeta.setDisplayName("End");

        List<String> loreoverworld = new ArrayList<>();
        loreoverworld.add(ChatColor.WHITE + "Click to select region");
        loreoverworld.add("   ");
        loreoverworld.add(ChatColor.GRAY + "Players (" + ChatColor.AQUA + overworldPlayerCount + ChatColor.GRAY + ")" );

        List<String> lorenether = new ArrayList<>();
        lorenether.add(ChatColor.WHITE + "Click to select region");
        lorenether.add("   ");
        lorenether.add(ChatColor.GRAY + "Players (" + ChatColor.AQUA + netherPlayerCount + ChatColor.GRAY + ")" );

        List<String> loreend = new ArrayList<>();
        loreend.add(ChatColor.WHITE + "Click to select region");
        loreend.add("   ");
        loreend.add(ChatColor.GRAY + "Players (" + ChatColor.AQUA + endPlayerCount + ChatColor.GRAY + ")" );

        overworldItemMeta.setLore(loreoverworld);
        netherItemMeta.setLore(lorenether);
        endItemMeta.setLore(loreend);


        overworldItem.setItemMeta(overworldItemMeta);
        netherItem.setItemMeta(netherItemMeta);
        endItem.setItemMeta(endItemMeta);



        inventory.setItem(11, overworldItem);
        inventory.setItem(13, netherItem);
        inventory.setItem(15, endItem);
        player.openInventory(inventory);

    }


}