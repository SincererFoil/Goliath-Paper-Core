package ch.mcserver.goliathPaperCore.module.anticheat.data;

import ch.mcserver.goliathPaperCore.common.database.mysql.AnticheatRepository;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;

public class AnticheatStateService {

    private final Plugin plugin;

    private final AnticheatRepository repository;

    private final PlayerDataManager playerDataManager;

    private final ExecutorService databaseExecutor = Executors.newSingleThreadExecutor();

    public AnticheatStateService(Plugin plugin, AnticheatRepository repository, PlayerDataManager playerDataManager) {
        this.plugin = plugin;
        this.repository = repository;
        this.playerDataManager = playerDataManager;
    }

    public void load(PlayerData playerData) {
        UUID uuid = playerData.getUuid();

        databaseExecutor.execute(() -> {

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
        databaseExecutor.execute(() -> {
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

        databaseExecutor.execute(() -> {
            repository.saveCheckStates(states);
        });
    }

    public void shutdown() {

        saveAll();

        databaseExecutor.shutdown();
        try {
            boolean finished = databaseExecutor.awaitTermination(5, TimeUnit.SECONDS);
            if (!finished) {
                plugin.getLogger().log(Level.WARNING, "AnticheatStateService shutdown timed out.");
                databaseExecutor.shutdownNow();
            }

        } catch (InterruptedException e) {
            databaseExecutor.shutdownNow();
            Thread.currentThread().interrupt();
        }

    }
}
