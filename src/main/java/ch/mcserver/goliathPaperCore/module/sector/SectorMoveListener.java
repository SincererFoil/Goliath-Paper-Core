package ch.mcserver.goliathPaperCore.module.sector;

import ch.mcserver.goliathPaperCore.GoliathPaperCore;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
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

    private final Plugin plugin;

    private final Set<UUID> pendingTransfers = ConcurrentHashMap.newKeySet();

    public SectorMoveListener(SectorRegistry sectorRegistry, SectorResolver sectorResolver, SectorTransferService sectorTransferService, Plugin plugin) {
        this.sectorRegistry = sectorRegistry;
        this.sectorResolver = sectorResolver;
        this.sectorTransferService = sectorTransferService;
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {

        if ((event.getFrom().getBlockX() >> 4) == (event.getTo().getBlockX() >> 4) && (event.getTo().getBlockZ() >> 4) == (event.getFrom().getBlockZ()>> 4) ) return;
        if (event.getFrom().getWorld().getEnvironment() != event.getTo().getWorld().getEnvironment()) return;
        if (sectorRegistry.isOwnSector(event.getTo().getWorld().getEnvironment(), event.getTo().getChunk().getX(), event.getTo().getChunk().getZ())) return;


        event.setCancelled(true);

        handleSectorChange(event.getPlayer(), event.getTo().clone());
    }

    @EventHandler
    public void onPlayerQuit() {

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
}
