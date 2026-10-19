package ch.mcserver.goliathPaperCore.module.nametag;

import ch.mcserver.goliathPaperCore.common.database.mysql.PlayerObject;
import ch.mcserver.goliathPaperCore.common.database.mysql.PlayerObjectManager;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

public class PlayerNametagManager {


    public void updateNametag(Player player) {

        PlayerObject playerObject = PlayerObjectManager.getPlayer(player.getUniqueId());


        boolean sfmode = playerObject != null && playerObject.isSfmode();

        boolean developer = player.hasPermission("goliath.rank.dev");

        boolean staff = player.hasPermission("goliath.rank.staff");

        boolean donutPlus = player.hasPermission("goliath.rank.donutplus");

        boolean donutPlusPlus = player.hasPermission("goliath.rank.donutplusplus");

        boolean donutPlusPlusPlus = player.hasPermission("goliath.rank.donutplusplusplus");



        Component displayName;

        Scoreboard board = Bukkit.getScoreboardManager().getMainScoreboard();

        Team team = board.getTeam(player.getName());

        if (team == null) {
            team = board.registerNewTeam(player.getName());
        }

        team.addEntry(player.getName());

        NamedTextColor nameColor = NamedTextColor.WHITE;

        boolean nameBold = false;

        if (developer) {
            if (sfmode) {
                displayName = Component.text("DEV ", NamedTextColor.DARK_PURPLE, TextDecoration.BOLD);
                nameColor = NamedTextColor.DARK_PURPLE;
            } else {
                displayName = Component.text("★", NamedTextColor.DARK_PURPLE);
            }
        } else if (staff) {
            if (sfmode) {
                displayName = Component.text("STAFF ", NamedTextColor.GREEN, TextDecoration.BOLD);
            } else {
                displayName = Component.text("★", NamedTextColor.GREEN);

            }
        } else if (donutPlusPlusPlus) {
            displayName = Component.text("+++", NamedTextColor.AQUA);
        } else if (donutPlusPlus) {
            displayName = Component.text("++", NamedTextColor.AQUA);
        } else if (donutPlus) {
            displayName = Component.text("+", NamedTextColor.AQUA);
        } else {
            displayName = Component.empty();
        }

        team.prefix(displayName);
        team.color(nameColor);



    }
}
