package ch.mcserver.goliathPaperCore.module.anticheat.data;

import ch.mcserver.goliathPaperCore.module.anticheat.flag.FlagManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class PlayerDataManager {

    private final Map<UUID, PlayerData> playerDataMap = new HashMap<>();
    private final FlagManager flagManager;

    public PlayerDataManager(FlagManager flagManager) {
        this.flagManager = flagManager;
    }

    public PlayerData create(UUID uuid, String username) {
        PlayerData playerData =
                new PlayerData(uuid, username, flagManager);

        playerDataMap.put(uuid, playerData);
        return playerData;
    }

    public PlayerData get(UUID uuid) {
        return playerDataMap.get(uuid);
    }

    public void remove(UUID uuid) {
        playerDataMap.remove(uuid);
    }

    public List<PlayerData> getAll() {
        return new ArrayList<>(playerDataMap.values());
    }
}