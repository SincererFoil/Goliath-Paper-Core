package ch.mcserver.goliathPaperCore.module.spawn;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockIgniteEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.entity.EntityTargetEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.player.PlayerBucketEmptyEvent;
import org.bukkit.event.player.PlayerBucketFillEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.weather.WeatherChangeEvent;

public class SpawnListener implements Listener {

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        World world = Bukkit.getWorld("DonutSpawn_");
        Location location = new Location(world, 499.42430123641094, 65.0, 401.46845676046337, 89.94278f, -4.200037f);
        player.teleport(location);
    }

    @EventHandler
    public void onPlayerDamage(EntityDamageByEntityEvent event) {
        World world = event.getEntity().getWorld();
        if (world.getName().equals("DonutSpawn_")) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        World world = event.getPlayer().getWorld();
        if (world.getName().equals("DonutSpawn_")) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        World world = event.getPlayer().getWorld();
        if (world.getName().equals("DonutSpawn_")) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlayerDropItem(PlayerDropItemEvent event) {
        World world = event.getPlayer().getWorld();
        if (world.getName().equals("DonutSpawn_")) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onEntityPickupItem(EntityPickupItemEvent event) {
        if (!(event.getEntity() instanceof Player player)) {
            return;
        }

        if (player.getWorld().getName().equals("DonutSpawn_")) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onFoodLevelChange(FoodLevelChangeEvent event) {
        World world = event.getEntity().getWorld();
        if (world.getName().equals("DonutSpawn_")) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onEntityExplode(EntityExplodeEvent event) {
        World world = event.getEntity().getWorld();
        if (world.getName().equals("DonutSpawn_")) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onBlockIgnite(BlockIgniteEvent event) {
        World world = event.getBlock().getWorld();

        if (world.getName().equals("DonutSpawn_")) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        World world = event.getPlayer().getWorld();
        if (world.getName().equals("DonutSpawn_")) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onWeatherChange(WeatherChangeEvent event) {
        World world = event.getWorld();
        if (world.getName().equals("DonutSpawn_")) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onEntitySpawn(CreatureSpawnEvent event) {
        World world = event.getEntity().getWorld();
        if (world.getName().equals("DonutSpawn_")) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onEntityTarget(EntityTargetEvent event) {
        World world = event.getEntity().getWorld();
        if (world.getName().equals("DonutSpawn_")) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlayerEmptyBucket(PlayerBucketEmptyEvent event) {
        World world = event.getPlayer().getWorld();
        if (world.getName().equals("DonutSpawn_")) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlayerBucketFill(PlayerBucketFillEvent event) {
        World world = event.getPlayer().getWorld();
        if (world.getName().equals("DonutSpawn_")) {
            event.setCancelled(true);
        }
    }
}
