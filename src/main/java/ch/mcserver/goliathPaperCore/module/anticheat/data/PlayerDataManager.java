package ch.mcserver.goliathPaperCore.module.anticheat.data;

import java.util.*;

public class PlayerDataManager {
    private final Map<UUID, PlayerData> playerDataMap = new HashMap<>();

    public PlayerData create(UUID uuid, String username) {
        PlayerData playerData = new PlayerData(uuid, username);
        playerDataMap.put(uuid, playerData);
        return playerData;
    }

    public PlayerData get(UUID uuid) {
        return playerDataMap.get(uuid);
    }
    public  void remove(UUID uuid) {
        playerDataMap.remove(uuid);
    }

    public List<PlayerData> getAll() {
        return new ArrayList<>(playerDataMap.values());
    }
}

