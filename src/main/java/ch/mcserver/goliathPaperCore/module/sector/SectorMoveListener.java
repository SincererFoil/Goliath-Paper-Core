package ch.mcserver.goliathPaperCore.module.sector;

import ch.mcserver.goliathPaperCore.GoliathPaperCore;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.util.Vector;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;

public class SectorMoveListener implements Listener {

    private final SectorRegistry sectorRegistry;

    private final SectorResolver sectorResolver;

    private final SectorTransferService sectorTransferService;

    private final GoliathPaperCore plugin;

    private final Set<UUID> pendingTransfers = ConcurrentHashMap.newKeySet();

    private final Map<UUID, Long> maintenanceCooldown = new HashMap<>();

    private final Map<UUID, Location> lastValidLocations = new HashMap<>();

    public SectorMoveListener(SectorRegistry sectorRegistry, SectorResolver sectorResolver, SectorTransferService sectorTransferService, GoliathPaperCore plugin) {
        this.sectorRegistry = sectorRegistry;
        this.sectorResolver = sectorResolver;
        this.sectorTransferService = sectorTransferService;
        this.plugin = plugin;
    }


    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        if (event.getTo() == null) return;

        Player player = event.getPlayer();
        Location from = event.getFrom();
        Location to = event.getTo();

        if (from.getWorld() != to.getWorld()) return;

        int toChunkX = to.getBlockX() >> 4;
        int toChunkZ = to.getBlockZ() >> 4;

        World.Environment dimension = to.getWorld().getEnvironment();

        if (player.getTicksLived() % 5 == 0) {
            SectorData sector = sectorRegistry.getOwnSector();

            showSectorBorder(player, to, sector.minChunkX(), sector.maxChunkX(), sector.minChunkZ(), sector.maxChunkZ());
        }

        UUID playerUuid = player.getUniqueId();

        if (sectorRegistry.isOwnSector(dimension, toChunkX, toChunkZ)) {
            if (sectorRegistry.isOwnSector(from.getWorld().getEnvironment(), from.getBlockX() >> 4, from.getBlockZ() >> 4)) {
                lastValidLocations.put(playerUuid, to.clone());
            }

            return;
        }

        if (pendingTransfers.contains(playerUuid)) {
            return;
        }

        handleSectorChange(player, to.clone());
    }


    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        UUID playerUuid = event.getPlayer().getUniqueId();

        pendingTransfers.remove(playerUuid);
        maintenanceCooldown.remove(playerUuid);
        lastValidLocations.remove(playerUuid);
    }


    private void handleSectorChange(Player player, Location location) {
        World.Environment dimension = location.getWorld().getEnvironment();
        int chunkX = location.getBlockX() >> 4;
        int chunkZ = location.getBlockZ() >> 4;

        double positionX = location.getX();
        double positionY = location.getY();
        double positionZ = location.getZ();

        float yaw = location.getYaw();
        float pitch = location.getPitch();

        UUID playerUuid = player.getUniqueId();

        if (!pendingTransfers.add(playerUuid)) return;

        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
            try {
                Optional<SectorData> sectorData =
                        sectorResolver.resolveByChunk(dimension, chunkX, chunkZ);

                if (sectorData.isEmpty()) {
                    Bukkit.getScheduler().runTask(plugin, () -> {
                        pendingTransfers.remove(playerUuid);

                        if (player.isOnline() && Bukkit.getPlayer(playerUuid) == player) {
                            dashBack(player);

                            player.playSound(player.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 1.0f, 1.0f);
                        }
                    });

                    return;
                }

                String targetServer = sectorData.get().serverName();

                if (!sectorRegistry.isSectorOnline(targetServer)) {
                    Bukkit.getScheduler().runTask(plugin, () -> {
                        if (!player.isOnline() || Bukkit.getPlayer(playerUuid) != player) {
                            pendingTransfers.remove(playerUuid);
                            return;
                        }

                        dashBack(player);

                        long now = System.currentTimeMillis();
                        long last = maintenanceCooldown.getOrDefault(playerUuid, 0L);

                        if (now - last >= 2000) {
                            maintenanceCooldown.put(playerUuid, now);

                            player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_NO, 1.0f, 1.0f);

                            Component message = Component.text("It seems that you are connection to an area in maintenance, try again i a few minutes.", NamedTextColor.RED);

                            player.sendActionBar(message);
                            player.sendMessage(message);
                        }

                        pendingTransfers.remove(playerUuid);
                    });

                    return;
                }

                SectorTransferData transferData = new SectorTransferData(UUID.randomUUID(), playerUuid, GoliathPaperCore.serverName, targetServer, sectorData.get().dimension(), positionX, positionY, positionZ, yaw, pitch);


                if (!sectorTransferService.publish(transferData)) {
                    Bukkit.getScheduler().runTask(plugin, () -> {
                        if (player.isOnline() && Bukkit.getPlayer(playerUuid) == player) {
                            dashBack(player);
                        }

                        pendingTransfers.remove(playerUuid);
                    });

                    return;
                }

            } catch (Exception e) {
                plugin.getLogger().log(Level.SEVERE, "Failed to resolve sector", e);

                Bukkit.getScheduler().runTask(plugin, () -> {
                    if (player.isOnline() && Bukkit.getPlayer(playerUuid) == player) {
                        dashBack(player);
                    }

                    pendingTransfers.remove(playerUuid);
                });
            }
        });
    }


    private void dashBack(Player player) {
        SectorData sector = sectorRegistry.getOwnSector();

        Location location = player.getLocation();

        double minX = sector.minChunkX() * 16.0;
        double maxX = (sector.maxChunkX() + 1) * 16.0;
        double minZ = sector.minChunkZ() * 16.0;
        double maxZ = (sector.maxChunkZ() + 1) * 16.0;

        double targetX = Math.max(minX + 3.0, Math.min(maxX - 3.0, location.getX()));
        double targetZ = Math.max(minZ + 3.0, Math.min(maxZ - 3.0, location.getZ()));

        Vector direction = new Vector(targetX - location.getX(), 0, targetZ - location.getZ());

        Location lastValid = lastValidLocations.get(player.getUniqueId());

        if (direction.lengthSquared() < 0.001) {
            if (lastValid != null && lastValid.getWorld() == location.getWorld()) {
                player.teleport(lastValid);
            }

            return;
        }

        direction.normalize().multiply(0.8);
        direction.setY(0.2);

        player.setVelocity(direction);

        if (!isInsideOwnSector(location)) {
            Bukkit.getScheduler().runTaskLater(plugin, () -> {
                if (!player.isOnline()) return;

                if (!isInsideOwnSector(player.getLocation())) {
                    Location safeLocation = lastValidLocations.get(player.getUniqueId());

                    if (safeLocation != null && safeLocation.getWorld() == player.getWorld() && isInsideOwnSector(safeLocation)) {
                        player.teleport(safeLocation);
                    }
                }
            }, 10L);
        }
    }


    private boolean isInsideOwnSector(Location location) {
        if (location.getWorld() == null) return false;

        return sectorRegistry.isOwnSector(location.getWorld().getEnvironment(), location.getBlockX() >> 4, location.getBlockZ() >> 4);
    }


    private void showSectorBorder(Player player, Location location, int minChunkX, int maxChunkX, int minChunkZ, int maxChunkZ) {

        double minX = minChunkX * 16.0;
        double maxX = (maxChunkX + 1) * 16.0;
        double minZ = minChunkZ * 16.0;
        double maxZ = (maxChunkZ + 1) * 16.0;

        double x = location.getX();
        double z = location.getZ();

        double nearestX = Math.abs(x - minX) < Math.abs(x - maxX) ? minX : maxX;
        double nearestZ = Math.abs(z - minZ) < Math.abs(z - maxZ) ? minZ : maxZ;

        double distanceX = Math.abs(x - nearestX);
        double distanceZ = Math.abs(z - nearestZ);

        if (Math.min(distanceX, distanceZ) > 48) return;

        boolean xBorder = distanceX <= distanceZ;
        double border = xBorder ? nearestX : nearestZ;

        Particle.DustOptions dust = new Particle.DustOptions(Color.fromRGB(65, 190, 255), 1.3f);

        for (double horizontal = -30; horizontal <= 30; horizontal += 0.8) {
            for (double vertical = -8; vertical <= 16; vertical += 0.8) {

                Location particle = xBorder ? new Location(location.getWorld(), border, location.getY() + vertical, z + horizontal) : new Location(location.getWorld(), x + horizontal, location.getY() + vertical, border);

                player.spawnParticle(Particle.DUST, particle, 1, 0, 0, 0, 0, dust);
            }
        }
    }
}