package ch.mcserver.goliathPaperCore.common.database.mysql;

import ch.mcserver.goliathPaperCore.GoliathPaperCore;
import ch.mcserver.goliathPaperCore.module.history.snapshot.HistorySnapshot;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PlayerObjectManager implements Listener {

    private static final Map<UUID, PlayerObject> players = new HashMap<>();

    public static void addPlayer(PlayerObject playerObject) {
        players.put(playerObject.getUuid(), playerObject);
    }

    public static PlayerObject getPlayer(UUID uuid) {
        return players.get(uuid);
    }

    public static boolean hasPlayer(UUID uuid) {
        return players.containsKey(uuid);
    }

    public static void removePlayer(UUID uuid) {
        players.remove(uuid);
    }

    @EventHandler
    public void onPlayerConnect(PlayerJoinEvent event) {

        Player player = event.getPlayer();
        UUID uuid = player.getUniqueId();

        if (!GoliathPaperCore.playerRepository.exists(uuid)) {
            player.kick(Component.text("Your player data does not exist."));
            return;
        }

        PlayerObject playerObject = GoliathPaperCore.playerRepository.loadPlayer(uuid);

        if (playerObject == null) {
            player.kick(Component.text("Your player data could not be loaded."));
            return;
        }

        playerObject.setSfmode(false);
        playerObject.setGmsp(false);

        addPlayer(playerObject);
    }

    @EventHandler
    public void onPlayerDisconnect(PlayerQuitEvent event) {

        Player player = event.getPlayer();

        PlayerObject playerObject = getPlayer(player.getUniqueId());

        if (playerObject == null) {
            return;
        }

        playerObject.setSfmode(false);
        playerObject.setGmsp(false);

        GoliathPaperCore.playerRepository.savePlayerDataOnly(playerObject);

        String serverName = GoliathPaperCore.config
                .node("server", "name")
                .getString("unknown");

        UUID historyId = UUID.randomUUID();

        GoliathPaperCore.getHistoryRepository().createEvent(
                player.getUniqueId(),
                "Disconnect",
                "PlayerQuit DISCONNECTING FREE",
                serverName,
                historyId.toString()
        );

        HistorySnapshot.createSnapshot(player.getUniqueId(), historyId, "Disconnect");

        removePlayer(player.getUniqueId());
    }
}
