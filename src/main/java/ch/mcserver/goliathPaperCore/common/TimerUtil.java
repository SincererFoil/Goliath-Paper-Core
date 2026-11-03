package ch.mcserver.goliathPaperCore.common;

import ch.mcserver.goliathPaperCore.GoliathPaperCore;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.HashMap;
import java.util.Set;
import java.util.UUID;

public class TimerUtil implements Listener {

    private GoliathPaperCore plugin;

    private final Set<UUID> timerPlayers = new HashMap<>().keySet();

    public TimerUtil(GoliathPaperCore plugin) {
        this.plugin = plugin;
    }

    public void executeWithTimer(int seconds, Runnable runnable, Player player, String eventText) {
        if (seconds < 1) {
            runnable.run();
            return;
        }

        BukkitTask timer = new BukkitRunnable() {
            int remaining = seconds;

            @Override
            public void run() {
                if (!player.isOnline()) {
                    cancel();
                    return;
                }

                if (remaining <= 0) {
                    cancel();
                    runnable.run();
                    return;
                }

                player.sendActionBar(
                        Component.text(eventText + " ", NamedTextColor.AQUA)
                                .append(Component.text(remaining, NamedTextColor.GRAY))
                );

                player.playSound(player, Sound.UI_BUTTON_CLICK, 1.0f, 1.0f);

                remaining--;
            }
        }.runTaskTimer(plugin, 0L, 20L);
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {

    }
}
