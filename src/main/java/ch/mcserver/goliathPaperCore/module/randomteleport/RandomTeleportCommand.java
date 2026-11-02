package ch.mcserver.goliathPaperCore.module.randomteleport;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class RandomTeleportCommand implements CommandExecutor {

    private final RandomTeleportGUI randomTeleportGUI = new RandomTeleportGUI();

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String @NotNull [] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(Component.text("This is a Player Command", NamedTextColor.RED));
            return false;
        }

        randomTeleportGUI.openMainGUI(player);
        return true;

    }
}
