package ch.mcserver.goliathPaperCore.module.sector;

import ch.mcserver.goliathPaperCore.common.database.mysql.SectorRepository;
import org.bukkit.Location;
import org.bukkit.World;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class SectorResolver {

    private final SectorRepository sectorRepository;

    public SectorResolver(SectorRepository sectorRepository) {
        this.sectorRepository = sectorRepository;
    }

    public Optional<SectorData> resolveByChunk(World.Environment dimension, int chunkX, int chunkZ) throws SQLException {

        return sectorRepository.findByChunk(dimension.name(), chunkX, chunkZ);
    }

    public Optional<SectorData> resolveByBlock(World.Environment dimension, int blockX, int blockZ) throws SQLException {
        int chunkX = Math.floorDiv(blockX, 16);
        int chunkZ = Math.floorDiv(blockZ, 16);

        return resolveByChunk(dimension, chunkX, chunkZ);
    }

    public Optional<SectorData> resolveByLocation(Location location) throws SQLException {
        return resolveByBlock(location.getWorld().getEnvironment(), location.getBlockX(), location.getBlockZ());
    }
}
