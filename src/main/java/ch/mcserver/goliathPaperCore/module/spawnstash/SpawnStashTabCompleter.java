package ch.mcserver.goliathPaperCore.module.spawnstash;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class SpawnStashTabCompleter implements TabCompleter {

    private static final List<String> TYPES = List.of(
            "cluster",
            "column",
            "line",
            "loot",
            "pedestal",
            "single"
    );

    @Override
    public @Nullable List<String> onTabComplete(
            @NotNull CommandSender sender,
            @NotNull Command command,
            @NotNull String label,
            @NotNull String[] args
    ) {
        if (args.length != 1) {
            return List.of();
        }

        String input = args[0].toLowerCase();

        List<String> completions = new ArrayList<>();

        for (String type : TYPES) {
            if (type.startsWith(input)) {
                completions.add(type);
            }
        }

        return completions;
    }
}
