package ch.mcserver.goliathPaperCore.service;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

public class CommandErrorService implements Listener {

    public static void sendMessage(CommandSender sender) {
        if (!(sender instanceof Player player)) return;

        String message = ChatColor.translateAlternateColorCodes('&', "&cThis command does not exist.");

        player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_AMBIENT, 1f, 1f);
        player.sendActionBar(message);
        player.sendMessage(message);
    }

    @EventHandler
    public void onPlayerCommand(PlayerCommandPreprocessEvent event) {
        String raw = event.getMessage();

        if (!raw.startsWith("/")) return;

        String label = raw.substring(1).split(" ")[0].toLowerCase(); // "gtp"

        boolean exists =
                Bukkit.getCommandMap().getCommand(label) != null ||
                        Bukkit.getHelpMap().getHelpTopic("/" + label) != null;

        if (!exists) {
            event.setCancelled(true);
            sendMessage(event.getPlayer());
        }
    }
}