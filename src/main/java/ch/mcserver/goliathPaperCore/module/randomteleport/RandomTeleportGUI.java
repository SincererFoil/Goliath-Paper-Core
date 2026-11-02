package ch.mcserver.goliathPaperCore.module.randomteleport;

import me.clip.placeholderapi.PlaceholderAPI;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class RandomTeleportGUI {



    public void openMainGUI(Player player) {

        RandomTeleportInventoryHolder holder = new RandomTeleportInventoryHolder(player.getUniqueId());

        Inventory randomTeleportInventory = Bukkit.createInventory(holder, 27);



        String overworldPlayerCount = PlaceholderAPI.setPlaceholders(null, "%overworld_players%");

        String netherPlayerCount = PlaceholderAPI.setPlaceholders(null, "%nether_players%");

        if (overworldPlayerCount.equals("%overworld_players%")) {
            overworldPlayerCount = "...";
        }

        if (overworldPlayerCount.equals("%nether_players%")) {
            overworldPlayerCount = "...";
        }

        ItemStack overworldItem = buildItem(Material.GRASS_BLOCK, Component.text("Overworld", NamedTextColor.GREEN).decorate(TextDecoration.BOLD));
        ItemMeta overworldMeta = overworldItem.getItemMeta();
        List<Component> lore = new ArrayList<>();
        lore.add(Component.text("Click to select region", NamedTextColor.WHITE));
        lore.add(Component.text(" "));
        lore.add(Component.text("Players (", NamedTextColor.GRAY)
                .append(Component.text(overworldPlayerCount, NamedTextColor.AQUA))
                .append(Component.text(")", NamedTextColor.GRAY)));
        overworldMeta.lore(lore);
        overworldItem.setItemMeta(overworldMeta);


        randomTeleportInventory.setItem(11, overworldItem);

        player.openInventory(randomTeleportInventory);




    }

    public void openOverworldGUI(Player player) {

        RandomTeleportInventoryHolder holder = new RandomTeleportInventoryHolder(player.getUniqueId());

        Inventory randomTeleportInventory = Bukkit.createInventory(holder, 27);



    }


    private static ItemStack buildItem(Material material, Component name) {
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        meta.displayName(name);
        item.setItemMeta(meta);
        return item;
    }
}
