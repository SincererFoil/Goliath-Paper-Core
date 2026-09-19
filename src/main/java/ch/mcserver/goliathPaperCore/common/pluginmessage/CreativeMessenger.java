package ch.mcserver.goliathPaperCore.common.pluginmessage;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerGameModeChangeEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.messaging.PluginMessageListener;
import org.jetbrains.annotations.NotNull;

import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class CreativeMessenger implements PluginMessageListener, Listener {

    private static final String CHANNEL = "goliath:creative";

    private final Plugin plugin;
    private final Set<UUID> serverTriggered =
            ConcurrentHashMap.newKeySet();

    public CreativeMessenger(Plugin plugin) {
        this.plugin = plugin;

        Bukkit.getMessenger().registerIncomingPluginChannel(
                plugin,
                CHANNEL,
                this
        );

        Bukkit.getMessenger().registerOutgoingPluginChannel(
                plugin,
                CHANNEL
        );
    }

    @Override
    public void onPluginMessageReceived(
            @NotNull String channel,
            @NotNull Player receiver,
            byte @NotNull [] message
    ) {
        if (!CHANNEL.equals(channel)) {
            return;
        }

        try {
            ByteArrayDataInput input =
                    ByteStreams.newDataInput(message);

            UUID uuid = UUID.fromString(input.readUTF());
            boolean enabled = input.readBoolean();

            if (!receiver.getUniqueId().equals(uuid)) {
                plugin.getLogger().warning(
                        "Rejected goliath:creative UUID mismatch. Receiver="
                                + receiver.getUniqueId()
                                + ", payload="
                                + uuid
                );
                return;
            }

            Player player = Bukkit.getPlayer(uuid);

            if (player == null || !player.isOnline()) {
                return;
            }

            serverTriggered.add(uuid);

            if (enabled) {
                player.setGameMode(GameMode.CREATIVE);
                player.setAllowFlight(true);
                player.setFlying(true);
            } else {
                player.setFlying(false);
                player.setAllowFlight(false);
                player.setGameMode(GameMode.SURVIVAL);
            }

            Bukkit.getScheduler().runTaskLater(
                    plugin,
                    () -> serverTriggered.remove(uuid),
                    2L
            );

        } catch (RuntimeException exception) {
            plugin.getLogger().warning(
                    "Rejected malformed goliath:creative payload: "
                            + exception.getMessage()
            );
        }
    }

    @EventHandler
    public void onGameModeChange(PlayerGameModeChangeEvent event) {
        Player player = event.getPlayer();
        UUID uuid = player.getUniqueId();

        if (serverTriggered.contains(uuid)) {
            return;
        }

        GameMode current = player.getGameMode();
        GameMode next = event.getNewGameMode();

        boolean toCreative =
                next == GameMode.CREATIVE;

        boolean fromCreative =
                current == GameMode.CREATIVE
                        && next != GameMode.CREATIVE;

        if (!toCreative && !fromCreative) {
            return;
        }

        boolean enabled = toCreative;

        Bukkit.getScheduler().runTask(
                plugin,
                () -> sendCreativeState(player, enabled)
        );
    }

    private void sendCreativeState(
            Player player,
            boolean enabled
    ) {
        if (!player.isOnline()) {
            return;
        }

        ByteArrayDataOutput output =
                ByteStreams.newDataOutput();

        output.writeUTF(
                player.getUniqueId().toString()
        );

        output.writeBoolean(enabled);

        player.sendPluginMessage(
                plugin,
                CHANNEL,
                output.toByteArray()
        );
    }
}