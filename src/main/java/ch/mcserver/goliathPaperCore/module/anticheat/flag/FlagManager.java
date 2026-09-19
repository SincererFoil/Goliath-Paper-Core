package ch.mcserver.goliathPaperCore.module.anticheat.flag;

import ch.mcserver.goliathPaperCore.GoliathPaperCore;
import ch.mcserver.goliathPaperCore.common.database.redis.RedisManager;
import ch.mcserver.goliathPaperCore.module.anticheat.data.PlayerData;
import ch.mcserver.goliathPaperCore.module.anticheat.flag.autopunish.AutopunishData;
import ch.mcserver.goliathPaperCore.module.anticheat.flag.autopunish.PunishReason;
import com.google.gson.Gson;
import org.bukkit.plugin.Plugin;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;

public class FlagManager {

    private final Plugin plugin;

    private final RedisManager redisManager;

    private final Gson gson = new Gson();

    private static final Map<FlagType, Integer> autoPunishFlagTypes = new HashMap<>(Map.of(FlagType.INVALID_A, 100));
    private static final Map<FlagType, PunishReason> autoPunishReasons = new HashMap<>(Map.of(FlagType.INVALID_A, PunishReason.CHEATING));


    public FlagManager(Plugin plugin, RedisManager redisManager) {
        this.plugin = plugin;
        this.redisManager = redisManager;
    }

    public void handleFlag(PlayerData playerData, FlagType checkName, int violationLevel, String details) {

        FlagData data = new FlagData(
                playerData.getUuid(),
                playerData.getUsername(),
                checkName,
                violationLevel,
                details,
                GoliathPaperCore.serverName,
                System.currentTimeMillis()
        );

        sendStaffAlert(data);
        handlePunishment(data);

    }

    public void sendStaffAlert(FlagData flagData) {
        plugin.getLogger().log(Level.INFO, "[AC] " + flagData.playerName() + " FAILED " + flagData.checkName() + " | VL = " + flagData.violations() + " | " + flagData.details());
        redisManager.publish("goliath:anticheat:flag", gson.toJson(flagData));
    }

    public void handlePunishment(FlagData flagData) {
        if (!autoPunishFlagTypes.containsKey(flagData.checkName())) {
            return;
        }

        int violationLimit = autoPunishFlagTypes.get(flagData.checkName());



        if (flagData.violations() >= violationLimit) {
            plugin.getLogger().log(Level.INFO, "AC auto punish executed... ");

            PunishReason reason = autoPunishReasons.get(flagData.checkName());

            if (reason == null) {
                plugin.getLogger().log(Level.WARNING, "[AC] Can't load the punish reason from the flag " + flagData.checkName());
            }

            AutopunishData data = new AutopunishData(flagData, reason);

            redisManager.publish("goliath:anticheat:autopunish", gson.toJson(data));
        }
    }

}
