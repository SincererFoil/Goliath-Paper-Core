package ch.mcserver.goliathPaperCore.module.randomteleport;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class RandomTeleportTabCompleter implements TabCompleter {
    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String @NotNull [] args) {
        if (args.length != 1) {
            return List.of();
        }

        String input = args[0].toLowerCase();

        return List.of("asia", "east", "end", "eu central", "eu west", "nether", "oceania", "west").stream().filter(region -> region.startsWith(input)).toList();
    }
}
