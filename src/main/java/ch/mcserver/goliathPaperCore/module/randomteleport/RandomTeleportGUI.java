package ch.mcserver.goliathPaperCore.module.randomteleport;

import ch.mcserver.goliathPaperCore.common.service.NumberFormatter;
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

        RandomTeleportInventoryHolder holder = new RandomTeleportInventoryHolder(player.getUniqueId(), true);

        Inventory randomTeleportInventory = Bukkit.createInventory(holder, 27,Component.text("ʀᴀɴᴅᴏᴍ ᴛᴇʟᴇᴘᴏʀᴛ", NamedTextColor.DARK_GRAY));

        String rawOverworldPlayerCount = PlaceholderAPI.setPlaceholders(null, "%overworld_players%");

        String rawNetherPlayerCount = PlaceholderAPI.setPlaceholders(null, "%nether_players%");

        String rawEndPlayerCount = PlaceholderAPI.setPlaceholders(null, "%end_players%");



        String overworldPlayerCount = NumberFormatter.format(rawOverworldPlayerCount);

        String netherPlayerCount = NumberFormatter.format(rawNetherPlayerCount);

        String endPlayerCount = NumberFormatter.format(rawEndPlayerCount);

        ItemStack overworldItem = buildItem(Material.GRASS_BLOCK, Component.text("ᴏᴠᴇʀᴡᴏʀʟᴅ", NamedTextColor.GREEN).decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE));
        ItemMeta overworldMeta = overworldItem.getItemMeta();
        List<Component> overworldLore = new ArrayList<>();
        overworldLore.add(Component.text("Click to select region", NamedTextColor.WHITE).decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE));
        overworldLore.add(Component.text(" ").decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE));
        overworldLore.add(Component.text("Players (", NamedTextColor.GRAY).decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE)
                .append(Component.text(overworldPlayerCount, NamedTextColor.AQUA).decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE))
                .append(Component.text(")", NamedTextColor.GRAY).decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE)));
        overworldMeta.lore(overworldLore);

        ItemStack netherItem = buildItem(Material.NETHERRACK, Component.text("ɴᴇᴛʜᴇʀ", NamedTextColor.GREEN).decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE));
        ItemMeta netherMeta = netherItem.getItemMeta();
        List<Component> netherLore = new ArrayList<>();
        netherLore.add(Component.text("Click to randomly teleport", NamedTextColor.WHITE).decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE));
        netherLore.add(Component.text(" ").decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE));
        netherLore.add(Component.text("Players (", NamedTextColor.GRAY).decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE)
                .append(Component.text(netherPlayerCount, NamedTextColor.AQUA).decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE))
                .append(Component.text(")", NamedTextColor.GRAY).decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE)));

        netherLore.add(Component.text("NA East (", NamedTextColor.GRAY).decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE)
                .append(Component.text("Calculating..." , NamedTextColor.AQUA).decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE))
                .append(Component.text(")", NamedTextColor.GRAY).decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE)));

        netherMeta.lore(netherLore);


        ItemStack endItem = buildItem(Material.END_STONE, Component.text("ᴇɴᴅ", NamedTextColor.GREEN).decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE));
        ItemMeta endMeta = endItem.getItemMeta();
        List<Component> endLore = new ArrayList<>();
        endLore.add(Component.text("Click to randomly teleport", NamedTextColor.WHITE).decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE));
        endLore.add(Component.text(" ").decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE));
        endLore.add(Component.text("Players (", NamedTextColor.GRAY).decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE)
                .append(Component.text(endPlayerCount, NamedTextColor.AQUA).decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE))
                .append(Component.text(")", NamedTextColor.GRAY).decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE)));

        endLore.add(Component.text("NA East (", NamedTextColor.GRAY).decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE)
                .append(Component.text("Calculating..." , NamedTextColor.AQUA).decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE))
                .append(Component.text(")", NamedTextColor.GRAY).decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE)));

        endMeta.lore(endLore);


        overworldItem.setItemMeta(overworldMeta);
        netherItem.setItemMeta(netherMeta);
        endItem.setItemMeta(endMeta);

        randomTeleportInventory.setItem(11, overworldItem);

        randomTeleportInventory.setItem(13, netherItem);

        randomTeleportInventory.setItem(15, endItem);

        player.openInventory(randomTeleportInventory);

    }

    public void openOverworldGUI(Player player) {


        RandomTeleportInventoryHolder holder = new RandomTeleportInventoryHolder(player.getUniqueId(), false);

        Inventory randomTeleportInventory = Bukkit.createInventory(holder, 27);

        String rawEuCentralPlayerCount = PlaceholderAPI.setPlaceholders(null, "%overworld_eu_central_players%");
        String rawEuWestPlayerCount = PlaceholderAPI.setPlaceholders(null, "%overworld_eu_west_players%");
        String rawNaEastPlayerCount = PlaceholderAPI.setPlaceholders(null, "%overworld_na_east_players%");
        String rawNaWestPlayerCount = PlaceholderAPI.setPlaceholders(null, "%overworld_na_west_players%");
        String rawAsiaPlayerCount = PlaceholderAPI.setPlaceholders(null, "%overworld_asia_players%");
        String rawOceaniaPlayerCount = PlaceholderAPI.setPlaceholders(null, "%overworld_oceania_players%");


        String euCentralPlayerCount = NumberFormatter.format(rawEuCentralPlayerCount);
        String euWestPlayerCount = NumberFormatter.format(rawEuWestPlayerCount);
        String naEastPlayerCount = NumberFormatter.format(rawNaEastPlayerCount);
        String naWestPlayerCount = NumberFormatter.format(rawNaWestPlayerCount);
        String asiaPlayerCount = NumberFormatter.format(rawAsiaPlayerCount);
        String oceaniaPlayerCount = NumberFormatter.format(rawOceaniaPlayerCount);




        ItemStack euCentralItem = buildItem(Material.GRASS_BLOCK, Component.text("ᴏᴠᴇʀᴡᴏʀʟᴅ", NamedTextColor.GREEN).decoration(TextDecoration.ITALIC, false));
        ItemMeta euCentralMeta = euCentralItem.getItemMeta();
        List<Component> euCentralLore = new ArrayList<>();
        euCentralLore.add(Component.text("Click to randomly teleport", NamedTextColor.WHITE).decoration(TextDecoration.ITALIC, false));
        euCentralLore.add(Component.text(" ").decoration(TextDecoration.ITALIC, false));
        euCentralLore.add(Component.text("Players (", NamedTextColor.GRAY).decoration(TextDecoration.ITALIC, false)
                .append(Component.text(euCentralPlayerCount, NamedTextColor.AQUA))
                .append(Component.text(")", NamedTextColor.GRAY)));
        euCentralLore.add(Component.text("EU Central (", NamedTextColor.GRAY).decoration(TextDecoration.ITALIC, false)
                .append(Component.text("Calculating...", NamedTextColor.AQUA))
                .append(Component.text(")", NamedTextColor.GRAY)));
        euCentralMeta.lore(euCentralLore);
        euCentralItem.setItemMeta(euCentralMeta);


        ItemStack euWestItem = buildItem(Material.GRASS_BLOCK, Component.text("ᴏᴠᴇʀᴡᴏʀʟᴅ", NamedTextColor.GREEN).decoration(TextDecoration.ITALIC, false));
        ItemMeta euWestMeta = euWestItem.getItemMeta();
        List<Component> euWestLore = new ArrayList<>();
        euWestLore.add(Component.text("Click to randomly teleport", NamedTextColor.WHITE).decoration(TextDecoration.ITALIC, false));
        euWestLore.add(Component.text(" ").decoration(TextDecoration.ITALIC, false));
        euWestLore.add(Component.text("Players (", NamedTextColor.GRAY).decoration(TextDecoration.ITALIC, false)
                .append(Component.text(euWestPlayerCount, NamedTextColor.AQUA))
                .append(Component.text(")", NamedTextColor.GRAY)));
        euWestLore.add(Component.text("EU West (", NamedTextColor.GRAY).decoration(TextDecoration.ITALIC, false)
                .append(Component.text("Calculating...", NamedTextColor.AQUA))
                .append(Component.text(")", NamedTextColor.GRAY)));
        euWestMeta.lore(euWestLore);
        euWestItem.setItemMeta(euWestMeta);


        ItemStack naEastItem = buildItem(Material.GRASS_BLOCK, Component.text("ᴏᴠᴇʀᴡᴏʀʟᴅ", NamedTextColor.GREEN).decoration(TextDecoration.ITALIC, false));
        ItemMeta naEastMeta = naEastItem.getItemMeta();
        List<Component> naEastLore = new ArrayList<>();
        naEastLore.add(Component.text("Click to randomly teleport", NamedTextColor.WHITE).decoration(TextDecoration.ITALIC, false));
        naEastLore.add(Component.text(" ").decoration(TextDecoration.ITALIC, false));
        naEastLore.add(Component.text("Players (", NamedTextColor.GRAY).decoration(TextDecoration.ITALIC, false)
                .append(Component.text(naEastPlayerCount, NamedTextColor.AQUA))
                .append(Component.text(")", NamedTextColor.GRAY)));
        naEastLore.add(Component.text("NA East (", NamedTextColor.GRAY).decoration(TextDecoration.ITALIC, false)
                .append(Component.text("Calculating...", NamedTextColor.AQUA))
                .append(Component.text(")", NamedTextColor.GRAY)));
        naEastMeta.lore(naEastLore);
        naEastItem.setItemMeta(naEastMeta);


        ItemStack naWestItem = buildItem(Material.GRASS_BLOCK, Component.text("ᴏᴠᴇʀᴡᴏʀʟᴅ", NamedTextColor.GREEN).decoration(TextDecoration.ITALIC, false));
        ItemMeta naWestMeta = naWestItem.getItemMeta();
        List<Component> naWestLore = new ArrayList<>();
        naWestLore.add(Component.text("Click to randomly teleport", NamedTextColor.WHITE).decoration(TextDecoration.ITALIC, false));
        naWestLore.add(Component.text(" ").decoration(TextDecoration.ITALIC, false));
        naWestLore.add(Component.text("Players (", NamedTextColor.GRAY).decoration(TextDecoration.ITALIC, false)
                .append(Component.text(naWestPlayerCount, NamedTextColor.AQUA))
                .append(Component.text(")", NamedTextColor.GRAY)));
        naWestLore.add(Component.text("NA West (", NamedTextColor.GRAY).decoration(TextDecoration.ITALIC, false)
                .append(Component.text("Calculating...", NamedTextColor.AQUA))
                .append(Component.text(")", NamedTextColor.GRAY)));
        naWestMeta.lore(naWestLore);
        naWestItem.setItemMeta(naWestMeta);


        ItemStack asiaItem = buildItem(Material.GRASS_BLOCK, Component.text("ᴏᴠᴇʀᴡᴏʀʟᴅ", NamedTextColor.GREEN).decoration(TextDecoration.ITALIC, false));
        ItemMeta asiaMeta = asiaItem.getItemMeta();
        List<Component> asiaLore = new ArrayList<>();
        asiaLore.add(Component.text("Click to randomly teleport", NamedTextColor.WHITE).decoration(TextDecoration.ITALIC, false));
        asiaLore.add(Component.text(" ").decoration(TextDecoration.ITALIC, false));
        asiaLore.add(Component.text("Players (", NamedTextColor.GRAY).decoration(TextDecoration.ITALIC, false)
                .append(Component.text(asiaPlayerCount, NamedTextColor.AQUA))
                .append(Component.text(")", NamedTextColor.GRAY)));
        asiaLore.add(Component.text("Asia (", NamedTextColor.GRAY).decoration(TextDecoration.ITALIC, false)
                .append(Component.text("Calculating...", NamedTextColor.AQUA))
                .append(Component.text(")", NamedTextColor.GRAY)));
        asiaMeta.lore(asiaLore);
        asiaItem.setItemMeta(asiaMeta);


        ItemStack oceaniaItem = buildItem(Material.GRASS_BLOCK, Component.text("ᴏᴠᴇʀᴡᴏʀʟᴅ", NamedTextColor.GREEN).decoration(TextDecoration.ITALIC, false));
        ItemMeta oceaniaMeta = oceaniaItem.getItemMeta();
        List<Component> oceaniaLore = new ArrayList<>();
        oceaniaLore.add(Component.text("Click to randomly teleport", NamedTextColor.WHITE).decoration(TextDecoration.ITALIC, false));
        oceaniaLore.add(Component.text(" ").decoration(TextDecoration.ITALIC, false));
        oceaniaLore.add(Component.text("Players (", NamedTextColor.GRAY).decoration(TextDecoration.ITALIC, false)
                .append(Component.text(oceaniaPlayerCount, NamedTextColor.AQUA))
                .append(Component.text(")", NamedTextColor.GRAY)));
        oceaniaLore.add(Component.text("Oceania (", NamedTextColor.GRAY).decoration(TextDecoration.ITALIC, false)
                .append(Component.text("Calculating...", NamedTextColor.AQUA))
                .append(Component.text(")", NamedTextColor.GRAY)));
        oceaniaMeta.lore(oceaniaLore);
        oceaniaItem.setItemMeta(oceaniaMeta);


        randomTeleportInventory.setItem(10, naWestItem);
        randomTeleportInventory.setItem(11, naEastItem);
        randomTeleportInventory.setItem(12, euWestItem);
        randomTeleportInventory.setItem(13, euCentralItem);
        randomTeleportInventory.setItem(14, asiaItem);
        randomTeleportInventory.setItem(15, oceaniaItem);


        player.openInventory(randomTeleportInventory);



    }


    private static ItemStack buildItem(Material material, Component name) {
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        meta.displayName(name);
        item.setItemMeta(meta);
        return item;
    }
}
