package ch.mcserver.goliathPaperCore.module.anticheat.flag;

import ch.mcserver.goliathPaperCore.GoliathPaperCore;
import ch.mcserver.goliathPaperCore.module.anticheat.checks.Check;
import ch.mcserver.goliathPaperCore.module.anticheat.data.PlayerData;

import java.sql.Date;
import java.sql.Timestamp;
import java.time.Instant;

public class FlagManager {


    public void handleFlag(PlayerData playerData, String checkName, Double violationLevel, String details) {

        FlagData data = new FlagData(
                playerData.getUuid(),
                playerData.getUsername(),
                checkName,
                violationLevel,
                details,
                GoliathPaperCore.serverName,
                System.currentTimeMillis()
        );

//        plugin.getServer().getScheduler().runTask(plugin, () -> {
//            sendStaffAlert
//            handlePunishment()
//        });

    }

    public void sendStaffAlert(FlagData flagData, PlayerData playerData) {
        // SaveMysql
        // Publish redis
    }

    public void handlePunishment(FlagData flagData, PlayerData playerData) {
        return;
    }

}
