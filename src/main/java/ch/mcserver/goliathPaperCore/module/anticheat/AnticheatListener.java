package ch.mcserver.goliathPaperCore.module.anticheat;


import ch.mcserver.goliathPaperCore.GoliathPaperCore;
import ch.mcserver.goliathPaperCore.module.anticheat.data.AnticheatStateService;

import ch.mcserver.goliathPaperCore.module.anticheat.data.player.PacketData.*;
import ch.mcserver.goliathPaperCore.module.anticheat.data.player.PlayerDataManager;
import ch.mcserver.goliathPaperCore.module.anticheat.data.player.playerdata.PlayerData;
import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.event.*;

import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.protocol.packettype.PacketTypeCommon;
import com.github.retrooper.packetevents.protocol.player.DiggingAction;
import com.github.retrooper.packetevents.protocol.teleport.RelativeFlag;
import com.github.retrooper.packetevents.util.Vector3i;
import com.github.retrooper.packetevents.wrapper.play.client.*;
import com.github.retrooper.packetevents.wrapper.play.server.*;
import com.mongodb.Block;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.Plugin;

import java.util.UUID;
import java.util.logging.Level;

public class AnticheatListener extends PacketListenerAbstract implements Listener {

    private final PlayerDataManager playerDataManager;

    private final AnticheatStateService stateService;

    private int nextVelocityPingId = Integer.MIN_VALUE;

    private final Plugin plugin;

    public AnticheatListener(Plugin plugin, PlayerDataManager playerDataManager, AnticheatStateService stateService) {
        super(PacketListenerPriority.NORMAL);
        this.plugin = plugin;
        this.playerDataManager = playerDataManager;
        this.stateService = stateService;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        PlayerData data = playerDataManager.create(player.getUniqueId(), player.getName());

        stateService.load(data);
    }

    @EventHandler
    public void onPlayerDisconnect(PlayerQuitEvent event) {
        Player player = event.getPlayer();

        PlayerData data = playerDataManager.get(player.getUniqueId());

        if (data == null) {
            GoliathPaperCore.getInstance().getLogger().log(Level.WARNING, "Anticheat couldn't save the player data for " + event.getPlayer().getName());
            return;
        }

        stateService.save(data);

        playerDataManager.remove(player.getUniqueId());
    }

    @PacketHandler
    public void onPacketReceive(PacketReceiveEvent event) {
        if (!(event.getPacketType() instanceof PacketType.Play.Client type)) {
            return;
        }

        switch (type) {

            case PLAYER_FLYING, PLAYER_POSITION, PLAYER_ROTATION, PLAYER_POSITION_AND_ROTATION:
                updateMovementData(event);
                break;

            case PLAYER_DIGGING:
                updateDig(event);
                break;

            case INTERACT_ENTITY:
                WrapperPlayClientInteractEntity packet =
                        new WrapperPlayClientInteractEntity(event);

                if (packet.getAction()
                        == WrapperPlayClientInteractEntity.InteractAction.ATTACK) {
                    updateAttack(event, packet.getEntityId());
                }
                break;

            case ATTACK:
                updateAttack(event, new WrapperPlayClientAttack(event).getEntityId());
                break;

            case HELD_ITEM_CHANGE:
                updateHeldSlot(event);
                break;

            case PLAYER_BLOCK_PLACEMENT:
                updateBlockInteract(event);
                break;
            case CLICK_WINDOW:
                updateInventoryClick(event);
                break;

            case CLOSE_WINDOW:
                updateCloseWindowClient(event);
                break;

            case TELEPORT_CONFIRM:
                teleportConfirm(event);
                break;
            case ENTITY_ACTION:
                updatePlayerAction(event);
                break;
            case USE_ITEM:
                updateUseItem(event);
                break;
            case PONG:
                handleVelocityPong(event);
                break;
            default:
                return;
        }
    }


    @PacketHandler
    public void onPacketSend(PacketSendEvent event) {
        if (!(event.getPacketType() instanceof PacketType.Play.Server type)) {
            return;
        }

        switch (type) {
            case OPEN_WINDOW -> updateOpenWindow(event);
            case CLOSE_WINDOW -> updateCloseWindowServer(event);
            case PLAYER_POSITION_AND_LOOK -> updateTeleport(event);
            case ENTITY_VELOCITY -> updateVelocity(event);
        }
    }

    private void updatePlayerAction(PacketReceiveEvent event) {
        WrapperPlayClientEntityAction packet = new WrapperPlayClientEntityAction(event);

        long receivedAt = System.nanoTime();

        PlayerActionPacketData data = new PlayerActionPacketData(
                event.getUser().getUUID(),
                event.getPacketType(),
                packet.getAction(),
                receivedAt

        );

        Bukkit.getScheduler().runTask(plugin, () -> {
            PlayerData playerData = playerDataManager.get(data.playerUuid());

            if (playerData == null) {
                return;
            }


            playerData.updatePlayerAction(data);

            if (playerData.isCheckStateLoaded()) {
                playerData.getCheckManager().handle(data);
            }

        });
    }


    private void updateUseItem(PacketReceiveEvent event) {
        WrapperPlayClientUseItem packet = new WrapperPlayClientUseItem(event);

        long receivedAt = System.nanoTime();

        UseItemPacketData data = new UseItemPacketData(
                event.getUser().getUUID(),
                event.getPacketType(),
                packet.getHand(),
                receivedAt
        );

        Bukkit.getScheduler().runTask(plugin, () -> {
            PlayerData playerData = playerDataManager.get(data.playerUuid());

            if (playerData == null) {
                return;
            }

            Player player = Bukkit.getPlayer(data.playerUuid());

            if (player == null) {
                return;
            }

            playerData.setHeldItem(player.getInventory().getItemInMainHand().clone());
            playerData.setOffHandItem(player.getInventory().getItemInOffHand().clone());

            playerData.updateUseItem(data);

            if (playerData.isCheckStateLoaded()) {
                playerData.getCheckManager().handle(data);
            }
        });




    }

    private void updateVelocity(PacketSendEvent event) {
        WrapperPlayServerEntityVelocity packet = new WrapperPlayServerEntityVelocity(event);

        long receivedAt = System.nanoTime();

        VelocityPacketData data = new VelocityPacketData(
                event.getUser().getUUID(),
                event.getPacketType(),
                packet.getVelocity().x,
                packet.getVelocity().y,
                packet.getVelocity().z,
                receivedAt
        );

        int entityId = packet.getEntityId();

        Bukkit.getScheduler().runTask(plugin, () -> {
            PlayerData playerData = playerDataManager.get(data.playerUuid());

            if (playerData == null) {
                return;
            }

            Player player = Bukkit.getPlayer(data.playerUuid());
            if (player == null || entityId != player.getEntityId()) {
                return;
            }

            playerData.updateVelocity(data);

            int pingId = nextVelocityPingId++;

            playerData.registerVelocityPing(pingId);

            PacketEvents.getAPI().getPlayerManager().sendPacket(player, new WrapperPlayServerPing(pingId));

            if (playerData.isCheckStateLoaded()) {
                playerData.getCheckManager().handle(data);
            }

        });


    }

    private void teleportConfirm(PacketReceiveEvent event) {
        WrapperPlayClientTeleportConfirm packet = new WrapperPlayClientTeleportConfirm(event);

        UUID uuid = event.getUser().getUUID();
        int teleportId = packet.getTeleportId();

        Bukkit.getScheduler().runTask(plugin, () -> {
            PlayerData playerData = playerDataManager.get(uuid);

            if (playerData == null) {
                return;
            }

            playerData.acknowledgeClientTeleport(teleportId);
        });
    }

    private void updateTeleport(PacketSendEvent event) {
        WrapperPlayServerPlayerPositionAndLook packet = new WrapperPlayServerPlayerPositionAndLook(event);

        long receivedAt = System.nanoTime();

        double rawX = packet.getPosition().x;
        double rawY = packet.getPosition().y;
        double rawZ = packet.getPosition().z;

        boolean relativeX = packet.isRelativeFlag(RelativeFlag.X);
        boolean relativeY = packet.isRelativeFlag(RelativeFlag.Y);
        boolean relativeZ = packet.isRelativeFlag(RelativeFlag.Z);

        UUID uuid = event.getUser().getUUID();
        PacketTypeCommon type = event.getPacketType();
        int teleportId = packet.getTeleportId();

        Bukkit.getScheduler().runTask(plugin, () -> {
            PlayerData playerData = playerDataManager.get(uuid);

            if (playerData == null) {
                return;
            }

            double targetX = relativeX ? playerData.getX() + rawX : rawX;
            double targetY = relativeY ? playerData.getY() + rawY : rawY;
            double targetZ = relativeZ ? playerData.getZ() + rawZ : rawZ;


            TeleportPacketData data = new TeleportPacketData(
                    uuid,
                    type,
                    targetX,
                    targetY,
                    targetZ,
                    teleportId,
                    receivedAt

            );

            playerData.updateTeleport(data);

            if (playerData.isCheckStateLoaded()) {
                playerData.getCheckManager().handle(data);
            }
        });
    }

    private void updateCloseWindowClient(PacketReceiveEvent event) {
        WrapperPlayClientCloseWindow packet = new WrapperPlayClientCloseWindow(event);
        long receivedAt = System.nanoTime();

        WindowPacketData data = new WindowPacketData(
                event.getUser().getUUID(),
                event.getPacketType(),
                null,
                packet.getWindowId(),
                false,
                receivedAt
        );

        Bukkit.getScheduler().runTask(plugin, () -> {
            PlayerData playerData = playerDataManager.get(data.playerUuid());

            if (playerData == null) {
                return;
            }


            playerData.updateCloseWindowClient(data);

            if (playerData.isCheckStateLoaded()) {
                playerData.getCheckManager().handle(data);
            }
        });

    }

    private void updateCloseWindowServer(PacketSendEvent event) {
        WrapperPlayServerCloseWindow packet = new WrapperPlayServerCloseWindow(event);
        long receivedAt = System.nanoTime();

        WindowPacketData data = new WindowPacketData(
                event.getUser().getUUID(),
                event.getPacketType(),
                null,
                packet.getWindowId(),
                false,
                receivedAt
        );

        Bukkit.getScheduler().runTask(plugin, () -> {
            PlayerData playerData = playerDataManager.get(data.playerUuid());

            if (playerData == null) {
                return;
            }


            playerData.updateWindowServer(data);

            if (playerData.isCheckStateLoaded()) {
                playerData.getCheckManager().handle(data);
            }
        });
    }

    public void updateOpenWindow(PacketSendEvent event) {
        WrapperPlayServerOpenWindow packet = new WrapperPlayServerOpenWindow(event);

        long receivedAt = System.nanoTime();

        WindowPacketData data = new WindowPacketData(
                event.getUser().getUUID(),
                event.getPacketType(),
                packet.getTitle(),
                packet.getContainerId(),
                true,
                receivedAt
        );

        Bukkit.getScheduler().runTask(plugin, () -> {
            PlayerData playerData = playerDataManager.get(data.playerUuid());

            if (playerData == null) {
                return;
            }


            playerData.updateWindowServer(data);

            if (playerData.isCheckStateLoaded()) {
                playerData.getCheckManager().handle(data);
            }
        });
    }

    private void updateInventoryClick(PacketReceiveEvent event) {
        WrapperPlayClientClickWindow packet = new WrapperPlayClientClickWindow(event);

        long receivedAt = System.nanoTime();

        InventoryClickPacketData data = new InventoryClickPacketData (
                event.getUser().getUUID(),
                event.getPacketType(),
                packet.getWindowId(),
                packet.getSlot(),
                packet.getButton(),
                packet.getWindowClickType(),
                packet.getCarriedItemStack(),
                receivedAt
        );

        Bukkit.getScheduler().runTask(plugin, () -> {
            PlayerData playerData = playerDataManager.get(data.playerUuid());

            if (playerData == null) {
                return;
            }


            playerData.updateInventoryClick(data);

            if (playerData.isCheckStateLoaded()) {
                playerData.getCheckManager().handle(data);
            }
        });

    }

    private void updateBlockInteract(PacketReceiveEvent event) {
        WrapperPlayClientPlayerBlockPlacement packet = new WrapperPlayClientPlayerBlockPlacement(event);

        long receivedAt = System.nanoTime();

        BlockInteractPacketData data = new BlockInteractPacketData(
                event.getUser().getUUID(),
                event.getPacketType(),
                packet.getBlockPosition().x,
                packet.getBlockPosition().y,
                packet.getBlockPosition().z,
                packet.getFaceId(),
                receivedAt
        );


        Bukkit.getScheduler().runTask(plugin, () -> {
            PlayerData playerData = playerDataManager.get(data.playerUuid());

            if (playerData == null) {
                return;
            }


            playerData.updateBlockInteraction(data);

            if (playerData.isCheckStateLoaded()) {
                playerData.getCheckManager().handle(data);
            }
        });

    }

    private void handleVelocityPong(PacketReceiveEvent event) {
        WrapperPlayClientPong packet = new WrapperPlayClientPong(event);

        UUID uuid = event.getUser().getUUID();
        int pingId = packet.getId();

        Bukkit.getScheduler().runTask(plugin, () -> {
            PlayerData data = playerDataManager.get(uuid);

            if (data == null) {
                return;
            }

            data.acknowledgeVelocityPing(pingId);
        });
    }


    private void updateHeldSlot(PacketReceiveEvent event) {
        WrapperPlayClientHeldItemChange packet = new WrapperPlayClientHeldItemChange(event);
        long receivedAt = System.nanoTime();

        HeldSlotPacketData data = new HeldSlotPacketData(
                event.getUser().getUUID(),
                event.getPacketType(),
                packet.getSlot(),
                receivedAt

        );

        Bukkit.getScheduler().runTask(plugin, () -> {
            PlayerData playerData = playerDataManager.get(data.playerUuid());

            if (playerData == null) {
                return;
            }

            if (playerData.getHeldSlot() != data.slot()) {
                playerData.releaseUseItem();
            }

            playerData.updateHeldSlot(data);



            if (playerData.isCheckStateLoaded()) {
                playerData.getCheckManager().handle(data);
            }
        });
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onPlayerConsume(PlayerItemConsumeEvent event) {
        PlayerData data = playerDataManager.get(event.getPlayer().getUniqueId());

        if (data == null) {
            return;
        }

        data.releaseUseItem();
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        PlayerData data = playerDataManager.get(event.getEntity().getUniqueId());

        if (data == null) {
            return;
        }

        data.releaseUseItem();
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onBlockBreak(BlockBreakEvent event) {
        PlayerData data = playerDataManager.get(event.getPlayer().getUniqueId());

        if (data == null) {
            return;
        }

        Vector3i position = new Vector3i(
                event.getBlock().getX(),
                event.getBlock().getY(),
                event.getBlock().getZ()
        );

        long now = System.nanoTime();

        data.setLastBrokenBlock(position);
        data.setLastBlockBreakTime(now);
        data.getBlockBreakHistory().add(new BlockBreakSample(position, now));

        if (data.getBlockBreakHistory().size() > 100) {
            data.getBlockBreakHistory().removeFirst();
        }
    }


    private void updateAttack(PacketReceiveEvent event, int entityId) {

        UUID uuid = event.getUser().getUUID();
        PacketTypeCommon type = event.getPacketType();

        long receivedAt = System.nanoTime();

        Bukkit.getScheduler().runTask(plugin, () -> {

            Player sender = Bukkit.getPlayer(uuid);
            String targetName = "Unknown";
            if (sender != null) {
                for (Entity entity : sender.getWorld().getEntities()) {
                    if (entity.getEntityId() == entityId) {
                        targetName = entity.getName();
                        break;
                    }
                }
            }

            AttackPacketData data = new AttackPacketData(uuid,
                    type,
                    entityId,
                    targetName,
                    receivedAt);

            PlayerData playerData = playerDataManager.get(data.playerUuid());

            if (playerData == null) {
                return;
            }

            playerData.updateAttack(data);

            if (playerData.isCheckStateLoaded()) {
                playerData.getCheckManager().handle(data);
            }
        });
    }



    private void updateMovementData(PacketReceiveEvent event) {

        WrapperPlayClientPlayerFlying packet = new WrapperPlayClientPlayerFlying(event);

        RotationPacketData rotation = packet.hasRotationChanged() ? new RotationPacketData(
                event.getUser().getUUID(),
                event.getPacketType(),
                packet.getLocation().getYaw(),
                packet.getLocation().getPitch()) : null;


        MovementPacketData movement = new MovementPacketData(
                event.getUser().getUUID(),
                event.getPacketType(),
                packet.getLocation().getX(),
                packet.getLocation().getY(),
                packet.getLocation().getZ(),
                packet.isOnGround(),
                packet.hasPositionChanged()
        );


            Bukkit.getScheduler().runTask(plugin, () -> {

                PlayerData playerData = playerDataManager.get(movement.playerUuid());

                if (playerData == null) {
                    return;
                }

                if (rotation != null) {
                    playerData.updateRotation(rotation);
                }

                playerData.updateMovement(movement);

                if (playerData.isCheckStateLoaded()) {
                    playerData.getCheckManager().handle(movement);
                }
            });



    }

    public void updateDig(PacketReceiveEvent event) {
        WrapperPlayClientPlayerDigging packet = new WrapperPlayClientPlayerDigging(event);

        long receivedAt = System.nanoTime();

        DigAction action;

        UUID uuid = event.getUser().getUUID();
        if (packet.getAction() == DiggingAction.RELEASE_USE_ITEM) {
            Bukkit.getScheduler().runTask(plugin, () -> {

                PlayerData playerData = playerDataManager.get(uuid);

                if (playerData == null) {
                    return;
                }

                playerData.releaseUseItem();

            });
        }

        switch (packet.getAction()) {
            case CANCELLED_DIGGING -> action = DigAction.ABORT_DESTROY_BLOCK;

            case FINISHED_DIGGING -> action = DigAction.STOP_DESTROY_BLOCK;

            case START_DIGGING -> action = DigAction.START_DESTROY_BLOCK;

            default -> {
                return;
            }
        }

        DigPacketData data = new DigPacketData(
                event.getUser().getUUID(),
                event.getPacketType(),
                packet.getBlockPosition().x,
                packet.getBlockPosition().y,
                packet.getBlockPosition().z,
                action,
                receivedAt
        );

        Bukkit.getScheduler().runTask(plugin, () -> {

            PlayerData playerData = playerDataManager.get(data.playerUuid());

            if (playerData == null) {
                return;
            }

            playerData.updateDig(data);

            if (playerData.isCheckStateLoaded()) {
                playerData.getCheckManager().handle(data);
            }
        });

    }


}