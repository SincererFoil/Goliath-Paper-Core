package ch.mcserver.goliathPaperCore.module.randomteleport;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class RandomTeleportCommand implements CommandExecutor {

    private final RandomTeleportGUI randomTeleportGUI = new RandomTeleportGUI();

    private final RandomTeleportService service;

    public RandomTeleportCommand(RandomTeleportService service) {
        this.service = service;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String @NotNull [] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(Component.text("This is a Player Command", NamedTextColor.RED));
            return false;
        }

        if (args.length >= 1) {
            Region region;
            World.Environment dimension = World.Environment.NORMAL;

            String input = String.join(" ", args).toLowerCase();

            switch (input) {
                case "asia" -> region = Region.ASIA;
                case "oceania" -> region = Region.OCEANIA;
                case "east" -> region = Region.NA_EAST;
                case "west" -> region = Region.NA_WEST;
                case "eu central" -> region = Region.EU_CENTRAL;
                case "eu west" -> region = Region.EU_WEST;
                case "nether" -> {
                    region = Region.NA_EAST;
                    dimension = World.Environment.NETHER;
                }
                case "end" -> {
                    region = Region.NA_EAST;
                    dimension = World.Environment.THE_END;
                }
                default -> {
                    region = Region.NA_EAST;
                }
            }

            RTPRequest request = new RTPRequest(player, region, dimension);
            service.requestRandomTeleport(request);

            return true;
        }

        randomTeleportGUI.openMainGUI(player);
        return true;

    }
}
