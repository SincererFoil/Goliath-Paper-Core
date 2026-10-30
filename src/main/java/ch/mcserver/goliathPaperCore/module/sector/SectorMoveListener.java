package ch.mcserver.goliathPaperCore.module.sector;

import ch.mcserver.goliathPaperCore.GoliathPaperCore;
import net.kyori.adventure.text.Component;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.Plugin;

import java.sql.SQLException;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;

public class SectorMoveListener implements Listener {

    private final SectorRegistry sectorRegistry;

    private final SectorResolver sectorResolver;

    private final SectorTransferService sectorTransferService;

    private final GoliathPaperCore  plugin;

    private final Set<UUID> pendingTransfers = ConcurrentHashMap.newKeySet();

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

        if (player.getTicksLived() % 5 == 0) {
            SectorData sector = sectorRegistry.getOwnSector();

            showSectorBorder(
                    player,
                    event.getTo(),
                    sector.minChunkX(),
                    sector.maxChunkX(),
                    sector.minChunkZ(),
                    sector.maxChunkZ()
            );
        }

        if ((event.getFrom().getBlockX() >> 4) == (event.getTo().getBlockX() >> 4) && (event.getTo().getBlockZ() >> 4) == (event.getFrom().getBlockZ()>> 4) ) return;
        if (event.getFrom().getWorld().getEnvironment() != event.getTo().getWorld().getEnvironment()) return;
        if (sectorRegistry.isOwnSector(event.getTo().getWorld().getEnvironment(), event.getTo().getChunk().getX(), event.getTo().getChunk().getZ())) return;


        event.setCancelled(true);

        handleSectorChange(event.getPlayer(), event.getTo().clone());
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        pendingTransfers.remove(event.getPlayer().getUniqueId());
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
                Optional<SectorData> sectorData = sectorResolver.resolveByChunk(dimension, chunkX, chunkZ);

                if (sectorData.isEmpty()) {
                    pendingTransfers.remove(playerUuid);
                    Bukkit.getScheduler().runTask(plugin, () -> {player.playSound(player, Sound.ENTITY_ENDERMAN_TELEPORT, 1.0f, 1.0f);});
                    return;
                }

                SectorTransferData transferData = new SectorTransferData(UUID.randomUUID(), playerUuid, GoliathPaperCore.serverName, sectorData.get().serverName(),
                        sectorData.get().dimension(), positionX, positionY, positionZ, yaw, pitch);

                if (!sectorTransferService.publish(transferData)) {
                    pendingTransfers.remove(playerUuid);
                    return;
                }

            } catch (Exception e) {
                plugin.getLogger().log(Level.SEVERE, "Failed to resolve sector", e);
                pendingTransfers.remove(playerUuid);
            }
        });
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

        if (Math.min(distanceX, distanceZ) > 12) return;

        boolean xBorder = distanceX <= distanceZ;
        double border = xBorder ? nearestX : nearestZ;

        Particle.DustOptions dust = new Particle.DustOptions(Color.fromRGB(65, 190, 255), 1.3f);

        for (double horizontal = -8; horizontal <= 8; horizontal += 0.8) {
            for (double vertical = -2; vertical <= 3; vertical += 0.8) {

                Location particle = xBorder ? new Location(location.getWorld(), border, location.getY() + vertical, z + horizontal) : new Location(location.getWorld(), x + horizontal, location.getY() + vertical, border);

                player.spawnParticle(Particle.DUST, particle, 1, 0, 0, 0, 0, dust);
            }
        }
    }
}
