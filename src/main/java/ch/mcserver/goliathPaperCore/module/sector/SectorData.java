package ch.mcserver.goliathPaperCore.module.sector;

import org.bukkit.World;

public record SectorData(long id, String serverName, String dimension, int minChunkX, int maxChunkX, int minChunkZ, int maxChunkZ) {
}
