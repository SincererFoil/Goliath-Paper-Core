package ch.mcserver.goliathPaperCore.module.spawnstash;

import ch.mcserver.goliathPaperCore.GoliathPaperCore;
import ch.mcserver.goliathPaperCore.common.database.mysql.PlayerObject;
import ch.mcserver.goliathPaperCore.common.service.CommandErrorService;
import ch.mcserver.goliathPaperCore.module.spawnstash.type.*;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

public class SpawnStashCommand implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String @NotNull [] args) {
        if (!(sender instanceof Player)) {
            return false;
        }
        Player player = (Player) sender;
        UUID uuid = player.getUniqueId();
        PlayerObject playerObject = GoliathPaperCore.playerRepository.loadPlayer(uuid);
        if (playerObject == null) {
            return false;
        }
        if (!playerObject.isSfmode()) {
            CommandErrorService.sendMessage(player);
            return false;
        }
        Location location = player.getLocation();
        String type;

        if (args.length == 0) {
            String[] randomTypes = {
                    "cluster",
                    "column",
                    "line",
                    "loot",
                    "pedestal",
                    "single"
            };

            type = randomTypes[ThreadLocalRandom.current().nextInt(randomTypes.length)];

        } else if (args.length == 1) {
            type = args[0].toLowerCase();

        } else {
            player.sendMessage(ChatColor.RED + "Usage: /spawnstash [type]");
            return true;
        }

        SpawnStash stash;

        switch (type) {
            case "cluster":
                return false;
            case "column":
                stash = new Column();
                break;
            case "line":
                stash = new Line();
                break;
            case "loot":
                stash = new Loot();
                break;
            case "pedestal":
                stash = new Pedestal();
                break;
            case "single":
                stash = new Single();
                break;
            default:
                player.sendMessage(ChatColor.RED + "Unknown stash type.");
                return true;
        }

        stash.spawn(location);

        player.sendMessage(ChatColor.GREEN + "Spawned stash: " + ChatColor.WHITE + stash.getName());
        player.sendActionBar(ChatColor.GREEN + "Spawned stash: " + ChatColor.WHITE + stash.getName());


        return true;
    }
}
