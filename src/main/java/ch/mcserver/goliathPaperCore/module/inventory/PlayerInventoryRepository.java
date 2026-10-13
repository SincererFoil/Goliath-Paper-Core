package ch.mcserver.goliathPaperCore.module.inventory;

import ch.mcserver.goliathPaperCore.GoliathPaperCore;
import ch.mcserver.goliathPaperCore.common.database.redis.RedisManager;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.UpdateOptions;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bson.Document;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Level;

import static ch.mcserver.goliathPaperCore.module.enderchest.PlayerEnderchestRepository.fromBase64;
import static ch.mcserver.goliathPaperCore.module.enderchest.PlayerEnderchestRepository.toBase64;

public class PlayerInventoryRepository {

    private final MongoCollection<Document> collection;
    private final Plugin plugin;

    private final Map<UUID, InventoryState> states = new HashMap<>();
    private final Map<UUID, Player> loadedPlayers = new HashMap<>();
    private final Map<UUID, String> lockTokens = new HashMap<>();
    private final Map<UUID, Document> pendingQuitSaves = new HashMap<>();

    private final RedisManager redisManager;

    public PlayerInventoryRepository(MongoCollection<Document> collection, Plugin plugin, RedisManager redisManager) {
        this.collection = collection;
        this.plugin = plugin;
        this.redisManager = redisManager;
    }

    public void saveInventory(UUID playerUUID, boolean disconnect) {
        Player player = Bukkit.getPlayer(playerUUID);
        if (player == null) return;
        if (loadedPlayers.get(playerUUID) != player) return;
        InventoryState state = states.get(playerUUID);

        if (state == null || state == InventoryState.LOADING) return;
        if (state == InventoryState.SAVING && !disconnect) return;


        Document update = new Document()
                .append("updated_at", System.currentTimeMillis())
                .append("armor", toBase64(player.getInventory().getArmorContents()))
                .append("inventory", toBase64(player.getInventory().getContents()))
                .append("offhand", toBase64(new ItemStack[]{
                        player.getInventory().getItemInOffHand()
                }));

        if (state == InventoryState.SAVE_FAILED) {
            if (disconnect) {
                pendingQuitSaves.put(playerUUID, update);
            }
            return;
        }

        if (state == InventoryState.SAVING) {
            pendingQuitSaves.put(playerUUID, update);
            return;
        }

        states.put(playerUUID, InventoryState.SAVING);
        writeSnapshot(playerUUID, update, disconnect);
    }

    public void loadInventory(UUID playerUUID, int attempts) {
        Player player = Bukkit.getPlayer(playerUUID);
        if (player == null) return;

        if (states.get(playerUUID) == InventoryState.SAVE_FAILED) {
            player.kick(Component.text("We dont know what happend here! Make a Ticket: #23dff", NamedTextColor.RED));
            return;
        }

        if (states.get(playerUUID) == InventoryState.SAVING || lockTokens.containsKey(playerUUID)) {
            Bukkit.getScheduler().runTaskLater(plugin, () -> {
                if (player.isOnline() && Bukkit.getPlayer(playerUUID) == player) {
                    loadInventory(playerUUID, attempts);
                }
            }, 2L);
            return;
        }


        states.put(playerUUID, InventoryState.LOADING);
        String ownerToken = UUID.randomUUID().toString();
        lockTokens.put(playerUUID, ownerToken);
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
            Document document;
            boolean acquired = false;
            try {

                acquired = acquireLock(playerUUID, ownerToken);

                if (!acquired) {
                    Bukkit.getScheduler().runTask(plugin, () -> {
                        if (!ownerToken.equals(lockTokens.get(playerUUID))) return;

                        lockTokens.remove(playerUUID);
                        states.remove(playerUUID);

                        if (!player.isOnline()
                                || Bukkit.getPlayer(playerUUID) != player) return;

                        if (attempts >= 20) {
                            player.kick(Component.text(
                                    "Your previous inventory session is still active. Please reconnect.",
                                    NamedTextColor.RED
                            ));
                            return;
                        }

                        Bukkit.getScheduler().runTaskLater(plugin, () -> {
                            if (player.isOnline()
                                    && Bukkit.getPlayer(playerUUID) == player) {
                                loadInventory(playerUUID, attempts + 1);
                            }
                        }, 10L);
                    });
                    return;
                }

                document = collection.find(Filters.eq("uuid", playerUUID.toString())).first();
            } catch (Exception e) {
                plugin.getLogger().log(Level.SEVERE, "Inventory load failed: " + playerUUID, e);

                Bukkit.getScheduler().runTask(plugin, () -> {
                    if (!ownerToken.equals(lockTokens.get(playerUUID))) return;

                    states.put(playerUUID, InventoryState.SAVE_FAILED);

                    if (player.isOnline() && Bukkit.getPlayer(playerUUID) == player) {
                        player.kick(Component.text("We dont know what happend here! Make a Ticket: #23dff", NamedTextColor.RED));
                    }
                });

                if (acquired) {
                    finishSession(playerUUID, ownerToken);
                }
                return;
            }

            Bukkit.getScheduler().runTask(plugin, () -> {
                if (!player.isOnline() || Bukkit.getPlayer(playerUUID) != player || !ownerToken.equals(lockTokens.get(playerUUID))) {
                    finishSession(playerUUID, ownerToken);
                    return;
                }
                if (document == null) {
                    loadedPlayers.put(playerUUID, player);
                    states.put(playerUUID, InventoryState.READY);
                    saveInventory(playerUUID, false);
                    return;
                }


                try {
                    player.getInventory().setContents(fromBase64(document.getString("inventory")));
                    player.getInventory().setArmorContents(fromBase64(document.getString("armor")));
                    ItemStack[] offhand = fromBase64(document.getString("offhand"));
                    if (offhand.length > 0) {
                        player.getInventory().setItemInOffHand(offhand[0]);
                    }
                    loadedPlayers.put(playerUUID, player);
                    states.put(playerUUID, InventoryState.READY);


                } catch (Exception e) {
                    player.kick(Component.text("We dont know what happend here! Make a Ticket: #23dff", NamedTextColor.RED));
                    finishSession(playerUUID, ownerToken);
                    GoliathPaperCore.getInstance().getLogger().log(Level.WARNING, "Could not load Inventory from " + player.getName() + ". Error message: ", e);

                }
            });
        });

    }


    private void writeSnapshot(UUID uuid, Document snapshot, boolean disconnect) {
        String ownerToken = lockTokens.get(uuid);
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
            try {
                var result = collection.updateOne(Filters.eq("uuid", uuid.toString()),
                        new Document("$set", snapshot),
                        new UpdateOptions().upsert(true)
                );

                if (!result.wasAcknowledged()) {
                    throw new IllegalStateException("Save not acknowledged!");
                }

                Bukkit.getScheduler().runTask(plugin, () -> {
                    Document pending = pendingQuitSaves.remove(uuid);

                    if (pending != null) {
                        writeSnapshot(uuid, pending, true);
                    } else if (disconnect) {
                        finishSession(uuid, ownerToken);
                    } else {
                        states.put(uuid, InventoryState.READY);
                    }
                });
            } catch (Exception exception) {
                plugin.getLogger().log(Level.WARNING, "Inventory save failed: " + uuid, exception);
                Bukkit.getScheduler().runTask(plugin, () -> {
                    pendingQuitSaves.putIfAbsent(uuid, snapshot);
                    states.put(uuid, InventoryState.SAVE_FAILED);

                    Player current = Bukkit.getPlayer(uuid);
                    if (current != null) {
                        current.kick(Component.text("We dont know what happend here! Make a Ticket: #23dff", NamedTextColor.RED));
                    }
                });
            }
        });
    }

    private void finishSession(UUID uuid, String token) {
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
            try {
                if (!releaseLock(uuid, token)) {
                    throw new IllegalStateException("Inventory lock owner mismatch");
                }

                Bukkit.getScheduler().runTask(plugin, () -> {
                    if (!token.equals(lockTokens.get(uuid))) return;

                    lockTokens.remove(uuid);
                    states.remove(uuid);
                    loadedPlayers.remove(uuid);
                });
            } catch (Exception e) {
                plugin.getLogger().log(Level.SEVERE, "Inventory unlock failed: " + uuid, e);
            }
        });
    }


    public boolean isLoaded(Player player) {
        return loadedPlayers.get(player.getUniqueId()) == player && states.get(player.getUniqueId()) != InventoryState.SAVE_FAILED;
    }


    private boolean acquireLock(UUID uuid, String ownerToken) {
        try (var jedis = redisManager.getConnection()) {
            return jedis.setnx("goliath:inventory:lock:" + uuid, ownerToken) == 1L;
        }
    }

    private boolean releaseLock(UUID uuid, String token) {
        try (var jedis = redisManager.getConnection()) {
            // lua
            String script = "if redis.call('get',KEYS[1]) == ARGV[1] then return redis.call('del',KEYS[1]) end return 0";

            return Long.valueOf(1).equals(jedis.eval(
                    script,
                    java.util.List.of("goliath:inventory:lock:" + uuid),
                    java.util.List.of(token)
            ));
        }
    }



}
