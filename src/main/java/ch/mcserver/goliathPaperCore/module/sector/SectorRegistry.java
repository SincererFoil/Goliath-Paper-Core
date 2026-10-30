package ch.mcserver.goliathPaperCore.module.sector;


import ch.mcserver.goliathPaperCore.GoliathPaperCore;
import ch.mcserver.goliathPaperCore.common.database.mysql.SectorRepository;
import org.bukkit.World;

import java.sql.SQLException;

public class SectorRegistry {

    private SectorRepository sectorRepository;

    private boolean initialized = false;

    private SectorData sectorData;


    public SectorRegistry(SectorRepository sectorRepository) {
        this.sectorRepository = sectorRepository;
    }

    public void initialize() {
        initialized = false;

        var config = GoliathPaperCore.getInstance().getConfig();

        boolean enabled = config.getBoolean("world-system.enabled");

        if (!enabled) {
            return;
        }

        String dimension = config.getString("world-system.dimension");

        int minChunkX = config.getInt("world-system.sector.min-chunk-x");
        int maxChunkX = config.getInt("world-system.sector.max-chunk-x");
        int minChunkZ = config.getInt("world-system.sector.min-chunk-z");
        int maxChunkZ = config.getInt("world-system.sector.max-chunk-z");

        String serverName = config.getString("server.name");

        if (serverName == null || serverName.isBlank()) {
            GoliathPaperCore.getInstance().getLogger().severe("Sector initialization failed: Invalid server name.");
            return;
        }

        if (dimension == null || !(dimension.equals("OVERWORLD") || dimension.equals("NETHER") || dimension.equals("END"))) {

            GoliathPaperCore.getInstance().getLogger().severe("Sector initialization failed: Invalid dimension.");
            return;
        }

        if (minChunkX > maxChunkX || minChunkZ > maxChunkZ) {
            GoliathPaperCore.getInstance().getLogger().severe("Sector initialization failed: Invalid chunk boundaries.");
            return;
        }

        String databaseDimension = switch (dimension) {
            case "OVERWORLD" -> "NORMAL";
            case "NETHER" -> "NETHER";
            case "END" -> "THE_END";
            default -> throw new IllegalStateException("Unexpected dimension");
        };

        try {
            SectorRepository.RegistrationResult result = sectorRepository.register(serverName, databaseDimension, minChunkX, maxChunkX, minChunkZ, maxChunkZ);

            switch (result) {
                case CREATED -> {
                    sectorData = new SectorData(0, serverName, dimension, minChunkX, minChunkZ, maxChunkX, maxChunkZ);
                    initialized = true;
                    GoliathPaperCore.getInstance().getLogger().info("Sector registered successfully: " + serverName);
                }

                case ALREADY_REGISTERED -> {
                    initialized = true;
                    sectorData = new SectorData(0, serverName, dimension, minChunkX, minChunkZ, maxChunkX, maxChunkZ);
                    GoliathPaperCore.getInstance().getLogger().info("Existing sector registration verified: " + serverName);
                }

                case NAME_CONFLICT -> {
                    GoliathPaperCore.getInstance().getLogger().severe("Sector registration failed: Server name conflict.");
                }

                case RANGE_CONFLICT -> {
                    GoliathPaperCore.getInstance().getLogger().severe("Sector registration failed: Chunk range overlaps another sector.");
                }
            }

        } catch (SQLException exception) {
            GoliathPaperCore.getInstance().getLogger().severe("Sector registration failed: Database error.");
            exception.printStackTrace();
        }
    }

    public boolean isInitialized() {
        return initialized;
    }

    public boolean isOwnSector(World.Environment dimension, int chunkX, int chunkZ) {
        return isInitialized() && dimension.name().equals(sectorData.dimension()) && chunkX <= sectorData.maxChunkX() && chunkZ <= sectorData.maxChunkZ() && chunkX >= sectorData.minChunkX() && chunkZ >= sectorData.minChunkZ();
    }

    public SectorData getOwnSector() {
        return sectorData;
    }


}
