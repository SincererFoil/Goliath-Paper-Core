package ch.mcserver.goliathPaperCore.module.sector;

import ch.mcserver.goliathPaperCore.GoliathPaperCore;
import ch.mcserver.goliathPaperCore.common.database.mysql.SectorRepository;
import ch.mcserver.goliathPaperCore.common.database.redis.RedisManager;
import org.bukkit.World;

import java.sql.SQLException;

public class SectorRegistry {

    private SectorRepository sectorRepository;

    private RedisManager redisManager;

    private boolean initialized = false;

    private SectorData sectorData;

    public SectorRegistry(SectorRepository sectorRepository, RedisManager redisManager) {
        this.sectorRepository = sectorRepository;
        this.redisManager = redisManager;
    }

    public void initialize() {
        initialized = false;
        sectorData = null;

        var config = GoliathPaperCore.config;

        boolean enabled = config.node("world-system", "enabled").getBoolean(false);

        if (!enabled) {
            return;
        }

        String dimension = config.node("world-system", "dimension").getString();

        int minChunkX = config.node("world-system", "sector", "min-chunk-x").getInt();
        int maxChunkX = config.node("world-system", "sector", "max-chunk-x").getInt();
        int minChunkZ = config.node("world-system", "sector", "min-chunk-z").getInt();
        int maxChunkZ = config.node("world-system", "sector", "max-chunk-z").getInt();

        String serverName = config.node("server", "name").getString();

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
                    sectorData = new SectorData(0, serverName, databaseDimension, minChunkX, maxChunkX, minChunkZ, maxChunkZ);

                    initialized = true;
                    GoliathPaperCore.getInstance().getLogger().info("Sector registered successfully: " + serverName);
                }

                case ALREADY_REGISTERED -> {
                    sectorData = new SectorData(0, serverName, databaseDimension, minChunkX, maxChunkX, minChunkZ, maxChunkZ);

                    initialized = true;
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
        return isInitialized() && sectorData != null && dimension.name().equals(sectorData.dimension()) && chunkX <= sectorData.maxChunkX() && chunkZ <= sectorData.maxChunkZ() && chunkX >= sectorData.minChunkX() && chunkZ >= sectorData.minChunkZ();
    }

    public SectorData getOwnSector() {
        return sectorData;
    }

    public boolean isSectorOnline(String serverName) {
        try (var jedis = redisManager.getConnection()) {
            return jedis.exists("goliath:network:heartbeat:paper:" + serverName);

        } catch (Exception e) {
            GoliathPaperCore.getInstance().getLogger().warning(
                    "Could not check sector heartbeat: " + serverName
            );
            return false;
        }
    }
}