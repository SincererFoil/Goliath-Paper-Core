package ch.mcserver.goliathPaperCore.module.anticheat.data;

import ch.mcserver.goliathPaperCore.common.database.mysql.AnticheatRepository;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AnticheatStateService {

    private final Plugin plugin;

    private final AnticheatRepository repository;

    private final PlayerDataManager playerDataManager;

    private ExecutorService databaseExcecutor = Executors.newSingleThreadExecutor();

    public AnticheatStateService(Plugin plugin, AnticheatRepository repository, PlayerDataManager playerDataManager) {
        this.plugin = plugin;
        this.repository = repository;
        this.playerDataManager = playerDataManager;
    }

    public void load(PlayerData playerData) {
        UUID uuid = playerData.getUuid();

        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
            List<CheckState> states = repository.loadCheckStates(uuid);

            Bukkit.getScheduler().runTask(plugin, () -> {
                if (playerDataManager.get(uuid) != playerData) {
                    return;
                }

                playerData.getCheckManager().applyStates(states);
                playerData.setCheckStateLoaded(true);
            });
        });
    }

    public void save(PlayerData playerData) {
        List<CheckState> states = playerData.getCheckManager().createStates();
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
            repository.saveCheckStates(states);
        });

    }

    public void saveAll() {

        List<CheckState> states = new ArrayList<>();

        playerDataManager.getAll().stream().filter(PlayerData::isCheckStateLoaded).forEach(playerData -> {
            states.addAll(playerData.getCheckManager().createStates());
        });

        if (states.isEmpty()) {
            return;
        }

        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
            repository.saveCheckStates(states);
        });
    }
}
