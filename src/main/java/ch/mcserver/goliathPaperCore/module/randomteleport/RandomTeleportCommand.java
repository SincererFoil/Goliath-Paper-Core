package ch.mcserver.goliathPaperCore.module.randomteleport;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import static ch.mcserver.goliathPaperCore.module.randomteleport.TeleportGUI.openGUI;

public class RandomTeleportCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String @NotNull [] args) {
        if (!(sender instanceof Player)) {
            return false;
        }
        Player player = (Player) sender;
        if (args.length <= 1) {
            openGUI(player);
            return true;
        }


        return false;
    }
}
