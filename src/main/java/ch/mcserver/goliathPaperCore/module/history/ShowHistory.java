package ch.mcserver.goliathPaperCore.module.history;

import ch.mcserver.goliathPaperCore.GoliathPaperCore;
import ch.mcserver.goliathPaperCore.common.database.mysql.PlayerObject;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.Inventory;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class ShowHistory implements Listener, CommandExecutor {



    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String @NotNull [] args) {

        if (args.length != 1) return false;

        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "Only players can execute this command.");
            return false;
        }

        Player player = (Player) sender;

        UUID staffUUID = player.getUniqueId();

        if (Bukkit.getPlayer(staffUUID) == null) {
            return false;
        }

        String targetName = args[0];

        if (!GoliathPaperCore.getPlayerRepository().existsByUsername(targetName)) {
            player.sendMessage(ChatColor.RED + "Player " + ChatColor.RED + targetName + " does not exist.");
            return false;
        }

        PlayerObject targetObject = GoliathPaperCore.getPlayerRepository().loadPlayerByUsername(targetName);
        UUID targetUUID = targetObject.getUuid();

        showHistory(player, targetUUID);
        return true;

    }

    public static void showHistory(Player staffPlayer, UUID targetUUID) {
        int sideCount = 0;
        Inventory historyInterface = Bukkit.createInventory(null, 54, "History " + sideCount);



        staffPlayer.openInventory(historyInterface);



    }
}
