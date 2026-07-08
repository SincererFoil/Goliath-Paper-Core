package ch.mcserver.goliathPaperCore.module.spawn;

import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerToggleFlightEvent;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import ch.mcserver.goliathPaperCore.GoliathPaperCore;

public class DoubleJumpBoostListener implements Listener {

    private final GoliathPaperCore plugin;
    private final Map<UUID, Long> cooldownMap = new ConcurrentHashMap<>();
    public final ArrayList<UUID> playersInFlightmode = new ArrayList<>();

    private static final long COOLDOWN_MS = 1000L;
    private static final double FORWARD_POWER = 1.1;
    private static final double UPWARD_POWER = 0.55;

    public DoubleJumpBoostListener(GoliathPaperCore plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();

        if (player.getGameMode() != GameMode.SURVIVAL) {
            return;
        }

        if (player.isOnGround() && !player.getAllowFlight() && !player.isFlying()) {
            player.setAllowFlight(true);
        }
    }

    @EventHandler
    public void onToggleFlight(PlayerToggleFlightEvent event) {

        Player player = event.getPlayer();

        if (player.getGameMode() != GameMode.SURVIVAL) {
            return;
        }
        if (playersInFlightmode.contains(player.getUniqueId())) {
            return;
        }
        UUID uuid = player.getUniqueId();

        event.setCancelled(true);
        player.setFlying(false);
        player.setAllowFlight(false);



        long now = System.currentTimeMillis();
        Long cooldownUntil = cooldownMap.get(uuid);
        if (cooldownUntil != null && cooldownUntil > now) {
            return;
        }

        cooldownMap.put(uuid, now + COOLDOWN_MS);
        boost(player);
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        cooldownMap.remove(event.getPlayer().getUniqueId());
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        cooldownMap.remove(event.getPlayer().getUniqueId());
    }

    private void boost(Player player) {
        Location loc = player.getLocation();
        Vector direction = loc.getDirection().setY(0).normalize();
        Vector velocity = direction.multiply(FORWARD_POWER);
        velocity.setY(UPWARD_POWER);

        player.setVelocity(velocity);
        player.getWorld().playSound(loc, Sound.BLOCK_SLIME_BLOCK_FALL, 1.0f, 1.2f);
    }
}
