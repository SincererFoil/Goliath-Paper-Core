package ch.mcserver.goliathPaperCore.module.enderchest.command;

import ch.mcserver.goliathPaperCore.GoliathPaperCore;
import ch.mcserver.goliathPaperCore.common.database.mysql.PlayerObject;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class GoliathEnderchestCommand implements CommandExecutor {

    private final ShowGoliathEnderchest showGoliathEnderchest;

    public GoliathEnderchestCommand(ShowGoliathEnderchest showGoliathEnderchest) {
        this.showGoliathEnderchest = showGoliathEnderchest;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String @NotNull [] args) {

        if (!(sender instanceof Player player)) {
            return false;
        }

        if (!player.hasPermission("goliath.echest.admin")) {
            return true;
        }

        if (args.length != 1) {
            player.sendMessage(Component.text("Usage: /echest <player>", NamedTextColor.RED));
            return true;
        }

        String targetName = args[0];

        if (!GoliathPaperCore.getPlayerRepository().existsByUsername(targetName)) {
            player.sendMessage(Component.text("The Player Does not Exist!", NamedTextColor.RED));
            return true;
        }

        PlayerObject targetObject = GoliathPaperCore.getPlayerRepository().loadPlayerByUsername(targetName);
        boolean editable = player.hasPermission("goliath.echest.*");

        showGoliathEnderchest.showEnderchest(player, targetObject.getUuid(), targetObject.getName(), editable);

        return true;
    }
}
