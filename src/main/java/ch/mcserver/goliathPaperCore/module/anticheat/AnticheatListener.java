package ch.mcserver.goliathPaperCore.module.anticheat;


import ch.mcserver.goliathPaperCore.module.anticheat.data.AnticheatStateService;
import ch.mcserver.goliathPaperCore.module.anticheat.data.player.PacketData.*;
import ch.mcserver.goliathPaperCore.module.anticheat.data.player.playerdata.PlayerData;
import ch.mcserver.goliathPaperCore.module.anticheat.data.player.PlayerDataManager;
import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.*;
import com.comphenix.protocol.wrappers.BlockPosition;
import com.comphenix.protocol.wrappers.EnumWrappers;
import com.comphenix.protocol.wrappers.WrappedEnumEntityUseAction;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.Plugin;

import java.util.UUID;

public class AnticheatListener extends PacketAdapter implements Listener {

    private final PlayerDataManager playerDataManager;

    private final AnticheatStateService stateService;

    public AnticheatListener(Plugin plugin, PlayerDataManager playerDataManager, AnticheatStateService stateService) {
        super(
                plugin,
                ListenerPriority.NORMAL,

                PacketType.Play.Client.POSITION,
                PacketType.Play.Client.POSITION_LOOK,
                PacketType.Play.Client.LOOK,

                PacketType.Play.Client.ENTITY_ACTION,
                PacketType.Play.Client.BLOCK_DIG,
                PacketType.Play.Client.HELD_ITEM_SLOT,
                PacketType.Play.Client.WINDOW_CLICK,
                PacketType.Play.Client.CLOSE_WINDOW,
                PacketType.Play.Client.USE_ITEM_ON,
                PacketType.Play.Client.USE_ENTITY,

                PacketType.Play.Server.OPEN_WINDOW,
                PacketType.Play.Server.CLOSE_WINDOW,
                PacketType.Play.Server.ENTITY_VELOCITY,
                PacketType.Play.Server.POSITION
        );

        this.playerDataManager = playerDataManager;
        this.stateService = stateService;
    }

    @Override
    public void onPacketReceiving(PacketEvent event) {

        PacketType type = event.getPacketType();

        boolean isMovement =
                type.equals(PacketType.Play.Client.POSITION)
                        || type.equals(PacketType.Play.Client.POSITION_LOOK)
                        || type.equals(PacketType.Play.Client.LOOK);

        boolean hasPosition =
                type.equals(PacketType.Play.Client.POSITION)
                        || type.equals(PacketType.Play.Client.POSITION_LOOK);

        boolean hasRotation =
                type.equals(PacketType.Play.Client.LOOK)
                        || type.equals(PacketType.Play.Client.POSITION_LOOK);



        if (isMovement) {
            updateMovement(event, type, hasPosition);
        }

        if (hasRotation) {
            updateRotation(event, type);
        }

        if (type.equals(PacketType.Play.Client.ENTITY_ACTION)) {
            updatePlayerAction(event, type);
        }

        if (type.equals(PacketType.Play.Client.BLOCK_DIG)) {
            updateDig(event, type);
        }

        if (type.equals(PacketType.Play.Client.HELD_ITEM_SLOT)) {
            updateHeldSlot(event, type);
        }

        if (type.equals(PacketType.Play.Client.WINDOW_CLICK)) {
            updateInventoryClick(event, type);
        }

        if (type.equals(PacketType.Play.Client.CLOSE_WINDOW)) {
            updateWindow(event, type, false);
        }

        if (type.equals(PacketType.Play.Client.USE_ITEM_ON)) {
            updateBlockPlace(event, type);
            updateBlockInteract(event, type);
        }

        if (type.equals(PacketType.Play.Client.USE_ENTITY)) {
            updateAttack(event, type);
        }
    }

    @Override
    public void onPacketSending(PacketEvent event) {

        PacketType type = event.getPacketType();

        if (type.equals(PacketType.Play.Server.OPEN_WINDOW)) {
            updateWindow(event, type, true);
        }

        if (type.equals(PacketType.Play.Server.CLOSE_WINDOW)) {
            updateWindow(event, type, false);
        }

        if (type.equals(PacketType.Play.Server.ENTITY_VELOCITY)) {
            updateVelocity(event, type);
        }

        if (type.equals(PacketType.Play.Server.POSITION)) {
            updateTeleport(event, type);
        }
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {

        PlayerData playerData = playerDataManager.create(
                event.getPlayer().getUniqueId(),
                event.getPlayer().getName()
        );

        stateService.load(playerData);
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {

        UUID uuid = event.getPlayer().getUniqueId();

        PlayerData playerData = playerDataManager.get(uuid);

        if (playerData != null && playerData.isCheckStateLoaded()) {
            stateService.save(playerData);
        }

        playerDataManager.remove(uuid);
    }

    private void updateTeleport(PacketEvent event, PacketType type) {

        double x = event.getPacket().getDoubles().read(0);
        double y = event.getPacket().getDoubles().read(1);
        double z = event.getPacket().getDoubles().read(2);

        TeleportPacketData data = new TeleportPacketData(
                event.getPlayer().getUniqueId(),
                type,
                x,
                y,
                z,
                System.nanoTime()
        );

        Bukkit.getScheduler().runTask(plugin, () -> {

            PlayerData playerData = playerDataManager.get(data.playerUuid());

            if (playerData == null) {
                return;
            }

            playerData.updateTeleport(data);

            if (playerData.isCheckStateLoaded()) {
                playerData.getCheckManager().handle(data);
            }
        });
    }

    private void updateVelocity(PacketEvent event, PacketType type) {

        int entityId = event.getPacket().getIntegers().read(0);

        if (entityId != event.getPlayer().getEntityId()) {
            return;
        }

        double velocityX = event.getPacket().getIntegers().read(1) / 8000.0;
        double velocityY = event.getPacket().getIntegers().read(2) / 8000.0;
        double velocityZ = event.getPacket().getIntegers().read(3) / 8000.0;

        VelocityPacketData data = new VelocityPacketData(
                event.getPlayer().getUniqueId(),
                type,
                velocityX,
                velocityY,
                velocityZ,
                System.nanoTime()
        );

        Bukkit.getScheduler().runTask(plugin, () -> {

            PlayerData playerData = playerDataManager.get(data.playerUuid());

            if (playerData == null) {
                return;
            }

            playerData.updateVelocity(data);

            if (playerData.isCheckStateLoaded()) {
                playerData.getCheckManager().handle(data);
            }
        });
    }

    private void updateAttack(PacketEvent event, PacketType type) {

        WrappedEnumEntityUseAction useAction =
                event.getPacket().getEnumEntityUseActions().read(0);

        if (useAction == null) {
            return;
        }

        if (useAction.getAction() != EnumWrappers.EntityUseAction.ATTACK) {
            return;
        }

        int targetEntityId = event.getPacket().getIntegers().read(0);

        String targetName = null;

        for (Entity entity : event.getPlayer().getWorld().getEntities()) {

            if (entity.getEntityId() == targetEntityId) {
                targetName = entity.getName();
                break;
            }
        }

        AttackPacketData data = new AttackPacketData(
                event.getPlayer().getUniqueId(),
                type,
                targetEntityId,
                targetName,
                System.nanoTime()
        );

        Bukkit.getScheduler().runTask(plugin, () -> {

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

    private void updateBlockPlace(PacketEvent event, PacketType type) {

        BlockPosition position =
                event.getPacket().getBlockPositionModifier().read(0);

        if (position == null) {
            return;
        }

        int face = 0;

        if (event.getPacket().getDirections().size() > 0) {
            face = event.getPacket().getDirections().read(0).ordinal();
        }

        BlockPlacePacketData data = new BlockPlacePacketData(
                event.getPlayer().getUniqueId(),
                type,
                position.getX(),
                position.getY(),
                position.getZ(),
                face,
                event.getPlayer().getInventory().getItemInMainHand(),
                System.nanoTime()
        );

        Bukkit.getScheduler().runTask(plugin, () -> {

            PlayerData playerData = playerDataManager.get(data.playerUuid());

            if (playerData == null) {
                return;
            }

            playerData.updateBlockPlace(data);

            if (playerData.isCheckStateLoaded()) {
                playerData.getCheckManager().handle(data);
            }
        });
    }

    private void updateBlockInteract(PacketEvent event, PacketType type) {

        BlockPosition position =
                event.getPacket().getBlockPositionModifier().read(0);

        if (position == null) {
            return;
        }

        int face = 0;

        if (event.getPacket().getDirections().size() > 0) {
            face = event.getPacket().getDirections().read(0).ordinal();
        }

        BlockInteractPacketData data = new BlockInteractPacketData(
                event.getPlayer().getUniqueId(),
                type,
                position.getX(),
                position.getY(),
                position.getZ(),
                face,
                System.nanoTime()
        );

        Bukkit.getScheduler().runTask(plugin, () -> {

            PlayerData playerData = playerDataManager.get(data.playerUuid());

            if (playerData == null) {
                return;
            }

            playerData.updateBlockInteract(data);

            if (playerData.isCheckStateLoaded()) {
                playerData.getCheckManager().handle(data);
            }
        });
    }

    private void updateWindow(PacketEvent event, PacketType type, boolean open) {

        String windowTitle = null;

        if (open && event.getPacket().getChatComponents().size() > 0) {
            windowTitle = event.getPacket().getChatComponents().read(0).getJson();
        }

        WindowPacketData data = new WindowPacketData(
                event.getPlayer().getUniqueId(),
                type,
                windowTitle,
                open
        );

        Bukkit.getScheduler().runTask(plugin, () -> {

            PlayerData playerData = playerDataManager.get(data.playerUuid());

            if (playerData == null) {
                return;
            }

            playerData.updateWindow(data);

            if (playerData.isCheckStateLoaded()) {
                playerData.getCheckManager().handle(data);
            }
        });
    }

    private void updateInventoryClick(PacketEvent event, PacketType type) {

        int windowId = event.getPacket().getIntegers().read(0);
        int slot = event.getPacket().getIntegers().read(1);
        int button = event.getPacket().getIntegers().read(2);

        InventoryClickPacketData data = new InventoryClickPacketData(
                event.getPlayer().getUniqueId(),
                type,
                windowId,
                slot,
                button,
                null,
                event.getPlayer().getItemOnCursor(),
                System.nanoTime()
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

    private void updateHeldSlot(PacketEvent event, PacketType type) {

        int slot = event.getPacket().getIntegers().read(0);

        HeldSlotPacketData data = new HeldSlotPacketData(
                event.getPlayer().getUniqueId(),
                type,
                slot,
                System.nanoTime()
        );

        Bukkit.getScheduler().runTask(plugin, () -> {

            PlayerData playerData = playerDataManager.get(data.playerUuid());

            if (playerData == null) {
                return;
            }

            playerData.updateHeldSlot(data);

            if (playerData.isCheckStateLoaded()) {
                playerData.getCheckManager().handle(data);
            }
        });
    }

    private void updateDig(PacketEvent event, PacketType type) {

        EnumWrappers.PlayerDigType protocolAction =
                event.getPacket().getPlayerDigTypes().read(0);

        BlockPosition position =
                event.getPacket().getBlockPositionModifier().read(0);

        if (position == null) {
            return;
        }

        DigAction action;

        switch (protocolAction) {

            case START_DESTROY_BLOCK:
                action = DigAction.START_DESTROY_BLOCK;
                break;

            case STOP_DESTROY_BLOCK:
                action = DigAction.STOP_DESTROY_BLOCK;
                break;

            case ABORT_DESTROY_BLOCK:
                action = DigAction.ABORT_DESTROY_BLOCK;
                break;

            default:
                return;
        }

        DigPacketData data = new DigPacketData(
                event.getPlayer().getUniqueId(),
                type,
                position.getX(),
                position.getY(),
                position.getZ(),
                action,
                System.nanoTime()
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

    private void updatePlayerAction(PacketEvent event, PacketType type) {

        EnumWrappers.PlayerAction protocolAction =
                event.getPacket().getPlayerActions().read(0);

        PlayerAction action;

        switch (protocolAction) {

            case START_SPRINTING:
                action = PlayerAction.START_SPRINTING;
                break;

            case STOP_SPRINTING:
                action = PlayerAction.STOP_SPRINTING;
                break;

            case START_SNEAKING:
                action = PlayerAction.START_SNEAKING;
                break;

            case STOP_SNEAKING:
                action = PlayerAction.STOP_SNEAKING;
                break;

            default:
                return;
        }

        PlayerActionPacketData data = new PlayerActionPacketData(
                event.getPlayer().getUniqueId(),
                type,
                action,
                System.nanoTime()
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

    private void updateRotation(PacketEvent event, PacketType type) {

        float yaw = event.getPacket().getFloat().read(0);
        float pitch = event.getPacket().getFloat().read(1);

        RotationPacketData data = new RotationPacketData(
                event.getPlayer().getUniqueId(),
                type,
                yaw,
                pitch
        );

        Bukkit.getScheduler().runTask(plugin, () -> {

            PlayerData playerData = playerDataManager.get(data.playerUuid());

            if (playerData == null) {
                return;
            }

            playerData.updateRotation(data);

            if (playerData.isCheckStateLoaded()) {
                playerData.getCheckManager().handle(data);
            }
        });
    }

    private void updateMovement(PacketEvent event, PacketType type, boolean hasPosition) {

        double x = hasPosition ? event.getPacket().getDoubles().read(0) : 0;
        double y = hasPosition ? event.getPacket().getDoubles().read(1) : 0;
        double z = hasPosition ? event.getPacket().getDoubles().read(2) : 0;

        boolean onGround = event.getPacket().getBooleans().read(0);

        MovementPacketData data = new MovementPacketData(
                event.getPlayer().getUniqueId(),
                type,
                x,
                y,
                z,
                hasPosition,
                onGround
        );

        Bukkit.getScheduler().runTask(plugin, () -> {

            PlayerData playerData = playerDataManager.get(data.playerUuid());

            if (playerData == null) {
                return;
            }

            playerData.updateMovement(data);

            if (playerData.isCheckStateLoaded()) {
                playerData.getCheckManager().handle(data);
            }
        });
    }
}