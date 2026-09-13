package ch.mcserver.goliathPaperCore.module.anticheat.flag;

import ch.mcserver.goliathPaperCore.GoliathPaperCore;
import ch.mcserver.goliathPaperCore.common.database.redis.RedisManager;
import ch.mcserver.goliathPaperCore.module.anticheat.data.PlayerData;
import com.google.gson.Gson;
import org.bukkit.plugin.Plugin;

import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;

public class FlagManager {

    private final Plugin plugin;

    private final RedisManager redisManager;

    private final Gson gson = new Gson();

    private static final Set<FlagType> autoPunishFlagTypes = new HashSet<>();

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
        if (autoPunishFlagTypes.contains(flagData.checkName())) {
            // TODO AUTOPUNISHMENT / SEND TO PROXY
        }
        return;
    }

}
