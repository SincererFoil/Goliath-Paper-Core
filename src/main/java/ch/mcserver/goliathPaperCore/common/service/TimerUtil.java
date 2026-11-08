package ch.mcserver.goliathPaperCore.common.service;

import ch.mcserver.goliathPaperCore.GoliathPaperCore;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class TimerUtil implements Listener {

    private final GoliathPaperCore plugin;
    private final Map<UUID, BukkitTask> timers = new HashMap<>();

    public TimerUtil(GoliathPaperCore plugin) {
        this.plugin = plugin;
    }

    public void executeWithTimer(int seconds, Runnable runnable, UUID playerUuid, String eventText) {
        Player player = Bukkit.getPlayer(playerUuid);
        cancelTimer(playerUuid);

        if (seconds < 1) {
            runnable.run();
            return;
        }

        BukkitTask timer = new BukkitRunnable() {

            private int remaining = seconds;

            @Override
            public void run() {
                if (!player.isOnline()) {
                    timers.remove(playerUuid);
                    cancel();
                    return;
                }

                if (remaining <= 0) {
                    timers.remove(playerUuid);
                    cancel();
                    player.sendActionBar(Component.empty());
                    runnable.run();
                    return;
                }

                player.sendActionBar(Component.text(eventText + " ", NamedTextColor.GRAY).append(Component.text(remaining + "s", NamedTextColor.AQUA)));

                player.playSound(player.getLocation(), Sound.UI_BUTTON_CLICK, 1.0f, 1.0f);

                remaining--;
            }
        }.runTaskTimer(plugin, 0L, 20L);

        timers.put(playerUuid, timer);
    }

    private boolean cancelTimer(UUID uuid) {
        BukkitTask timer = timers.remove(uuid);

        if (timer == null) {
            return false;
        }

        timer.cancel();

        return true;
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onPlayerMove(PlayerMoveEvent event) {
        if (!event.hasChangedBlock()) {
            return;
        }

        Player player = event.getPlayer();

        if (cancelTimer(player.getUniqueId())) {
            player.sendActionBar(Component.text("Teleport cancelled because you moved", NamedTextColor.RED));
            player.sendMessage(Component.text("Teleport cancelled because you moved", NamedTextColor.RED));
            player.playSound(player, Sound.ENTITY_VILLAGER_AMBIENT, 1.0f, 1.0f);
        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        cancelTimer(event.getPlayer().getUniqueId());
    }
}