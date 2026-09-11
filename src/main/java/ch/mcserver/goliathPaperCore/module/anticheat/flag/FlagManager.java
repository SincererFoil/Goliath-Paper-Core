package ch.mcserver.goliathPaperCore.module.anticheat.flag;

import ch.mcserver.goliathPaperCore.GoliathPaperCore;
import ch.mcserver.goliathPaperCore.module.anticheat.checks.Check;
import ch.mcserver.goliathPaperCore.module.anticheat.data.PlayerData;
import org.bukkit.plugin.Plugin;

import java.sql.Date;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import static org.bukkit.Bukkit.getServer;

public class FlagManager {

    private static Plugin plugin = GoliathPaperCore.getInstance();

    private static final List<FlagType> autoPunishFlagTypes = new ArrayList<>();

    public static void handleFlag(PlayerData playerData, FlagType checkName, Double violationLevel, String details) {

        FlagData data = new FlagData(
                playerData.getUuid(),
                playerData.getUsername(),
                checkName,
                violationLevel,
                details,
                GoliathPaperCore.serverName,
                System.currentTimeMillis()
        );

        plugin.getServer().getScheduler().runTask(plugin, () -> {
            sendStaffAlert(data, playerData);
            handlePunishment(data, playerData);
        });

    }

    public static void sendStaffAlert(FlagData flagData, PlayerData playerData) {
        // Publish redis
    }

    public static void handlePunishment(FlagData flagData, PlayerData playerData) {
        if (autoPunishFlagTypes.contains(flagData.checkName())) {
            // TODO AUTOPUNISHMENT / SEND TO PROXY
        }
        return;
    }

}
