package ch.mcserver.goliathPaperCore.common.service;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerCommandSendEvent;

import java.util.Locale;
import java.util.Set;

public class CommandErrorService implements Listener {

    private static final Set<String> BLOCKED_COMMANDS = Set.of(
            "plugins",
            "pl",
            "version",
            "ver",
            "about",
            "icanhasbukkit",
            "commands",
            "help",
            "?",
            "paper",
            "velocity",
            "bukkit",
            "spigot",
            "purpur",
            "spark",
            "sparkc",
            "timings",
            "tps",
            "mspt",
            "debug",
            "perf",
            "jfr",
            "reload",
            "rl",
            "restart",
            "stop",
            "save-all",
            "save-on",
            "save-off",
            "op",
            "deop",
            "whitelist",
            "ban",
            "ban-ip",
            "pardon",
            "pardon-ip",
            "kick",
            "execute",
            "function",
            "schedule",
            "data",
            "datapack",
            "attribute",
            "bossbar",
            "forceload",
            "setidletimeout",
            "gamerule",
            "difficulty",
            "defaultgamemode",
            "worldborder",
            "reloadpermissions",
            "luckperms",
            "lp",
            "lpb",
            "anticheat"
    );

    public static void sendMessage(CommandSender sender) {
        if (!(sender instanceof Player player)) return;

        String message = ChatColor.translateAlternateColorCodes('&', "&cThis command does not exist.");

        player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_AMBIENT, 1f, 1f);
        player.sendActionBar(message);
        player.sendMessage(message);
    }

    @EventHandler
    public void onPlayerCommand(PlayerCommandPreprocessEvent event) {
        String raw = event.getMessage().trim();

        if (!raw.startsWith("/") || raw.length() == 1) return;

        String label = raw.substring(1).split("\\s+")[0].toLowerCase(Locale.ROOT);
        String commandName = label.contains(":") ? label.substring(label.indexOf(":") + 1) : label;

        if (!event.getPlayer().hasPermission("goliath.commandblock.bypass")
                && BLOCKED_COMMANDS.contains(commandName)) {
            event.setCancelled(true);
            sendMessage(event.getPlayer());
            return;
        }

        boolean exists = Bukkit.getCommandMap().getCommand(label) != null
                || Bukkit.getCommandMap().getCommand(commandName) != null
                || Bukkit.getHelpMap().getHelpTopic("/" + label) != null;

        if (!exists) {
            event.setCancelled(true);
            sendMessage(event.getPlayer());
        }
    }

    @EventHandler
    public void onCommandSend(PlayerCommandSendEvent event) {
        if (event.getPlayer().hasPermission("goliath.commandblock.bypass")) return;

        event.getCommands().removeIf(command -> {
            String label = command.toLowerCase(Locale.ROOT);
            String commandName = label.contains(":") ? label.substring(label.indexOf(":") + 1) : label;
            return BLOCKED_COMMANDS.contains(commandName);
        });
    }
}