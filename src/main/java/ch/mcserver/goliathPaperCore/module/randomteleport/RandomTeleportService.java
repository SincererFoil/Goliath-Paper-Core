package ch.mcserver.goliathPaperCore.module.randomteleport;

import ch.mcserver.goliathPaperCore.GoliathPaperCore;
import ch.mcserver.goliathPaperCore.common.database.mysql.SectorRepository;
import ch.mcserver.goliathPaperCore.common.database.redis.RedisManager;
import ch.mcserver.goliathPaperCore.common.service.TimerUtil;
import ch.mcserver.goliathPaperCore.module.sector.SectorData;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.scheduler.BukkitRunnable;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPubSub;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadLocalRandom;

public class RandomTeleportService implements Listener {

    private static final String TRANSFER_CHANNEL = "goliath:rtp:transfer";
    private static final String RESPONSE_CHANNEL = "goliath:rtp:response";
    private static final String ERROR_CHANNEL = "goliath:rtp:error";
    private static final String PENDING_PREFIX = "goliath:rtp:pending:";

    private static final int NETHER_SCAN_MAX_Y = 120;
    private static final int NETHER_SCAN_MIN_Y = 33;

    private final RedisManager redisManager;
    private final SectorRepository sectorRepository;
    private final GoliathPaperCore plugin;
    private final TimerUtil timerUtil;
    private final Gson gson = new Gson();

    private final ExecutorService subscriberExecutor = Executors.newSingleThreadExecutor();
    private final ExecutorService workerExecutor = Executors.newFixedThreadPool(2);

    private volatile boolean running;
    private volatile Jedis activeConnection;
    private volatile JedisPubSub activeSubscriber;

    private boolean listenerRegistered;

    public RandomTeleportService(RedisManager redisManager, SectorRepository sectorRepository, GoliathPaperCore plugin, TimerUtil timerUtil) {
        this.redisManager = redisManager;
        this.sectorRepository = sectorRepository;
        this.plugin = plugin;
        this.timerUtil = timerUtil;
    }

    public void requestRandomTeleport(RTPRequest request) {

        Player player = Bukkit.getPlayer(request.getPlayerUuid());

        if (player == null) return;

        if (player.hasPermission("goliath.timer.bypass")) {
            executeRandomTeleport(request);
            return;
        }

        BukkitRunnable runnable = new BukkitRunnable() {
            @Override
            public void run() {
                executeRandomTeleport(request);
            }
        };

        timerUtil.executeWithTimer(5, runnable, request.getPlayerUuid(), "Teleporting in");
    }

    public void executeRandomTeleport(RTPRequest request) {
        Player player = Bukkit.getPlayer(request.getPlayerUuid());

        if (player == null) {
            return;
        }

        try {
            redisManager.publish("goliath:rtp:request", gson.toJson(request));
        } catch (Exception exception) {
            plugin.getLogger().warning("RTP request failed: " + exception.getMessage());
            player.playSound(player, Sound.ENTITY_VILLAGER_AMBIENT, 1.0f, 1.0f);
            player.sendMessage(Component.text("The RTP is currently in maintenance, Please wait a few minutes and try again.", NamedTextColor.RED));
        }
    }

    public synchronized void startSubscriber() {
        if (running) return;

        if (!listenerRegistered) {
            Bukkit.getPluginManager().registerEvents(this, plugin);
            listenerRegistered = true;
        }

        running = true;
        subscriberExecutor.submit(this::subscribeLoop);
    }

    public synchronized void stopSubscriber() {
        running = false;

        JedisPubSub subscriber = activeSubscriber;
        if (subscriber != null) {
            subscriber.unsubscribe();
        }

        Jedis connection = activeConnection;
        if (connection != null) {
            connection.close();
        }

        subscriberExecutor.shutdownNow();
        workerExecutor.shutdownNow();
    }

    private void subscribeLoop() {
        while (running) {
            try (Jedis jedis = redisManager.getConnection()) {
                activeConnection = jedis;

                JedisPubSub subscriber = new JedisPubSub() {
                    @Override
                    public void onMessage(String channel, String message) {
                        if (!running || message == null || message.isBlank()) return;

                        try {
                            switch (channel) {
                                case TRANSFER_CHANNEL -> handleRandomTeleportTransfer(message);
                                case ERROR_CHANNEL -> handleRandomTeleportError(message);
                                default -> {
                                }
                            }
                        } catch (Exception exception) {
                            plugin.getLogger().warning("RTP subscriber failed: " + exception.getMessage());
                        }
                    }
                };

                activeSubscriber = subscriber;
                jedis.subscribe(subscriber, TRANSFER_CHANNEL, ERROR_CHANNEL);

            } catch (Exception exception) {
                if (running) {
                    plugin.getLogger().warning("RTP Redis connection failed: " + exception.getMessage());
                }
            } finally {
                activeSubscriber = null;
                activeConnection = null;
            }

            if (!running) break;

            try {
                Thread.sleep(3000);
            } catch (InterruptedException exception) {
                Thread.currentThread().interrupt();
                break;
            }
        }
    }

    private void handleRandomTeleportTransfer(String json) {
        JsonObject data = JsonParser.parseString(json).getAsJsonObject();

        if (!data.has("server") || data.get("server").isJsonNull()) return;
        if (!data.has("requestData") || !data.get("requestData").isJsonObject()) return;

        String targetServer = data.get("server").getAsString();

        if (!targetServer.equals(GoliathPaperCore.serverName)) return;

        JsonObject requestData = data.getAsJsonObject("requestData");

        if (!requestData.has("playerUuid") || !requestData.has("requestUuid")) return;

        UUID playerUuid = UUID.fromString(requestData.get("playerUuid").getAsString());
        UUID requestUuid = UUID.fromString(requestData.get("requestUuid").getAsString());

        if (!GoliathPaperCore.hasSectorEnabled) {
            publishError(requestData);
            return;
        }

        workerExecutor.submit(() -> {
            try {
                SectorData sector = sectorRepository
                        .findByServerName(GoliathPaperCore.serverName)
                        .orElse(null);

                if (sector == null) {
                    plugin.getLogger().warning("RTP sector not found in MySQL: " + GoliathPaperCore.serverName);
                    publishError(requestData);
                    return;
                }

                Bukkit.getScheduler().runTask(plugin, () -> {
                    prepareSectorTeleport(sector, playerUuid, requestUuid, targetServer, json, requestData);
                });

            } catch (Exception exception) {
                plugin.getLogger().warning("RTP sector lookup failed: " + exception.getMessage());
                publishError(requestData);
            }
        });
    }

    private void prepareSectorTeleport(SectorData sector, UUID playerUuid, UUID requestUuid, String targetServer, String responseJson, JsonObject requestData) {
        World.Environment environment = switch (sector.dimension()) {
            case "NORMAL" -> World.Environment.NORMAL;
            case "NETHER" -> World.Environment.NETHER;
            case "THE_END" -> World.Environment.THE_END;
            default -> null;
        };

        if (environment == null) {
            plugin.getLogger().warning("RTP unsupported sector dimension: " + sector.dimension());
            publishError(requestData);
            return;
        }

        World world = Bukkit.getWorlds().stream()
                .filter(w -> w.getEnvironment() == environment)
                .findFirst()
                .orElse(null);

        if (world == null) {
            plugin.getLogger().warning("RTP world not found: " + sector.dimension());
            publishError(requestData);
            return;
        }

        int minChunkX = sector.minChunkX();
        int maxChunkX = sector.maxChunkX();
        int minChunkZ = sector.minChunkZ();
        int maxChunkZ = sector.maxChunkZ();

        if (minChunkX > maxChunkX || minChunkZ > maxChunkZ) {
            plugin.getLogger().warning("RTP invalid sector boundaries: " + targetServer);
            publishError(requestData);
            return;
        }

        long minX = (long) minChunkX * 16L + 32L;
        long maxX = ((long) maxChunkX + 1L) * 16L - 33L;
        long minZ = (long) minChunkZ * 16L + 32L;
        long maxZ = ((long) maxChunkZ + 1L) * 16L - 33L;

        if (minX > maxX || minZ > maxZ ||
                minX < -29999984L || maxX > 29999984L ||
                minZ < -29999984L || maxZ > 29999984L) {
            plugin.getLogger().warning("RTP invalid block boundaries: " + targetServer);
            publishError(requestData);
            return;
        }

        int attempts = switch (environment) {
            case NETHER -> 40;
            case THE_END -> 60;
            default -> 20;
        };

        searchSafeLocation(world, (int) minX, (int) maxX, (int) minZ, (int) maxZ, attempts)
                .thenAccept(location -> {
                    if (location == null) {
                        plugin.getLogger().warning("RTP could not find safe location: " + targetServer);
                        publishError(requestData);
                        return;
                    }

                    prepareRandomTeleport(playerUuid, requestUuid, targetServer, location, responseJson, requestData);
                })
                .exceptionally(exception -> {
                    plugin.getLogger().warning("RTP location search failed: " + exception.getMessage());
                    publishError(requestData);
                    return null;
                });
    }

    private CompletableFuture<Location> searchSafeLocation(World world, int minX, int maxX, int minZ, int maxZ, int attempts) {
        if (attempts <= 0) {
            return CompletableFuture.completedFuture(null);
        }

        int x = ThreadLocalRandom.current().nextInt(minX, maxX + 1);
        int z = ThreadLocalRandom.current().nextInt(minZ, maxZ + 1);

        return findSafeLocation(world, x, z).thenCompose(location -> {
            if (location != null) {
                return CompletableFuture.completedFuture(location);
            }

            return searchSafeLocation(world, minX, maxX, minZ, maxZ, attempts - 1);
        });
    }

    private CompletableFuture<Location> findSafeLocation(World world, int x, int z) {
        int chunkX = Math.floorDiv(x, 16);
        int chunkZ = Math.floorDiv(z, 16);

        CompletableFuture<Location> future = new CompletableFuture<>();

        world.getChunkAtAsync(chunkX, chunkZ, true).whenComplete((chunk, exception) -> {
            if (exception != null || chunk == null) {
                future.complete(null);
                return;
            }

            Bukkit.getScheduler().runTask(plugin, () -> {
                try {
                    int y = findGroundY(world, x, z);

                    if (y < world.getMinHeight() || y + 2 >= world.getMaxHeight()) {
                        future.complete(null);
                        return;
                    }

                    Block ground = world.getBlockAt(x, y, z);
                    Block feet = world.getBlockAt(x, y + 1, z);
                    Block head = world.getBlockAt(x, y + 2, z);

                    if (!ground.getType().isSolid()) {
                        future.complete(null);
                        return;
                    }

                    if (!feet.isPassable() || !head.isPassable()) {
                        future.complete(null);
                        return;
                    }

                    if (ground.isLiquid() || feet.isLiquid() || head.isLiquid()) {
                        future.complete(null);
                        return;
                    }

                    if (isDangerous(ground.getType()) || isDangerous(feet.getType()) || isDangerous(head.getType())) {
                        future.complete(null);
                        return;
                    }

                    Location location = new Location(world, x + 0.5, y + 1, z + 0.5);

                    if (!world.getWorldBorder().isInside(location)) {
                        future.complete(null);
                        return;
                    }

                    future.complete(location);

                } catch (Exception exception1) {
                    future.completeExceptionally(exception1);
                }
            });
        });

        return future;
    }

    private int findGroundY(World world, int x, int z) {
        if (world.getEnvironment() != World.Environment.NETHER) {
            return world.getHighestBlockYAt(x, z, HeightMap.MOTION_BLOCKING_NO_LEAVES);
        }

        for (int y = NETHER_SCAN_MAX_Y; y >= NETHER_SCAN_MIN_Y; y--) {
            Block ground = world.getBlockAt(x, y, z);

            if (!ground.getType().isSolid() || ground.getType() == Material.BEDROCK) continue;
            if (!world.getBlockAt(x, y + 1, z).isPassable()) continue;
            if (!world.getBlockAt(x, y + 2, z).isPassable()) continue;

            return y;
        }

        return world.getMinHeight() - 1;
    }

    private boolean isDangerous(Material material) {
        return switch (material) {
            case LAVA, MAGMA_BLOCK, CACTUS, FIRE, SOUL_FIRE, CAMPFIRE, SOUL_CAMPFIRE, POWDER_SNOW, SWEET_BERRY_BUSH, WITHER_ROSE -> true;
            default -> false;
        };
    }

    private void prepareRandomTeleport(UUID playerUuid, UUID requestUuid, String targetServer, Location location, String responseJson, JsonObject requestData) {
        workerExecutor.submit(() -> {
            try {
                PreparedLocation preparedLocation = new PreparedLocation(
                        requestUuid,
                        playerUuid,
                        targetServer,
                        location.getWorld().getName(),
                        location.getX(),
                        location.getY(),
                        location.getZ(),
                        location.getYaw(),
                        location.getPitch()
                );

                String key = PENDING_PREFIX + playerUuid;

                try (Jedis jedis = redisManager.getConnection()) {
                    jedis.setex(key, 60, gson.toJson(preparedLocation));
                    jedis.publish(RESPONSE_CHANNEL, responseJson);
                }

                Bukkit.getScheduler().runTask(plugin, () -> {
                    Player player = Bukkit.getPlayer(playerUuid);

                    if (player != null && player.isOnline()) {
                        teleportPreparedPlayer(player, preparedLocation);
                    }
                });

            } catch (Exception exception) {
                plugin.getLogger().warning("RTP preparation failed: " + exception.getMessage());
                publishError(requestData);
            }
        });
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        Bukkit.getScheduler().runTaskLater(plugin, () -> {
            loadPreparedLocation(player);
        }, 1L);
    }

    private void loadPreparedLocation(Player player) {
        UUID playerUuid = player.getUniqueId();

        workerExecutor.submit(() -> {
            try (Jedis jedis = redisManager.getConnection()) {
                String json = jedis.get(PENDING_PREFIX + playerUuid);

                if (json == null) return;

                PreparedLocation preparedLocation = gson.fromJson(json, PreparedLocation.class);

                if (preparedLocation == null || !playerUuid.equals(preparedLocation.playerUuid())) return;
                if (!GoliathPaperCore.serverName.equals(preparedLocation.targetServer())) return;

                Bukkit.getScheduler().runTask(plugin, () -> {
                    if (!player.isOnline()) return;

                    teleportPreparedPlayer(player, preparedLocation);
                });

            } catch (Exception exception) {
                plugin.getLogger().warning("Could not load RTP location: " + exception.getMessage());
            }
        });
    }

    private void teleportPreparedPlayer(Player player, PreparedLocation preparedLocation) {
        if (!GoliathPaperCore.serverName.equals(preparedLocation.targetServer())) return;
        if (!player.getUniqueId().equals(preparedLocation.playerUuid())) return;

        World world = Bukkit.getWorld(preparedLocation.world());

        if (world == null) return;

        Location location = new Location(
                world,
                preparedLocation.x(),
                preparedLocation.y(),
                preparedLocation.z(),
                preparedLocation.yaw(),
                preparedLocation.pitch()
        );

        player.teleportAsync(location).thenAccept(success -> {
            if (!success) {
                plugin.getLogger().warning("RTP teleport failed: " + player.getUniqueId());
                return;
            }

            Bukkit.getScheduler().runTask(plugin, () -> {
                if (!player.isOnline()) return;

                player.sendActionBar(Component.text("Teleported to a Random Location", NamedTextColor.GRAY));
                player.playSound(player.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 1.0f, 1.0f);
            });

            workerExecutor.submit(() -> {
                try (Jedis jedis = redisManager.getConnection()) {
                    String key = PENDING_PREFIX + player.getUniqueId();
                    String stored = jedis.get(key);

                    if (stored == null) return;

                    PreparedLocation current = gson.fromJson(stored, PreparedLocation.class);

                    if (current != null && preparedLocation.requestUuid().equals(current.requestUuid())) {
                        jedis.del(key);
                    }
                } catch (Exception exception) {
                    plugin.getLogger().warning("Could not clear RTP location: " + exception.getMessage());
                }
            });
        });
    }

    private void handleRandomTeleportError(String json) {
        RTPRequest request = gson.fromJson(json, RTPRequest.class);

        if (request == null || request.getPlayerUuid() == null) return;

        Bukkit.getScheduler().runTask(plugin, () -> {
            Player player = Bukkit.getPlayer(request.getPlayerUuid());
            if (player == null) return;

            player.playSound(player, Sound.ENTITY_VILLAGER_NO, 1.0f, 1.0f);
            player.sendMessage(Component.text("The RTP is currently in maintenance, Please wait a few minutes and try again.", NamedTextColor.RED));
        });
    }

    private void publishError(JsonObject requestData) {
        workerExecutor.submit(() -> {
            try (Jedis jedis = redisManager.getConnection()) {
                jedis.publish(ERROR_CHANNEL, gson.toJson(requestData));
            } catch (Exception exception) {
                plugin.getLogger().warning("Could not publish RTP error: " + exception.getMessage());
            }
        });
    }
}
