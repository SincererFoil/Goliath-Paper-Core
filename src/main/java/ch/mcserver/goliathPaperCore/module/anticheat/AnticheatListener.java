package ch.mcserver.goliathPaperCore.module.anticheat;


import ch.mcserver.goliathPaperCore.GoliathPaperCore;
import ch.mcserver.goliathPaperCore.module.anticheat.data.AnticheatStateService;

import ch.mcserver.goliathPaperCore.module.anticheat.data.player.PacketData.*;
import ch.mcserver.goliathPaperCore.module.anticheat.data.player.PlayerDataManager;
import ch.mcserver.goliathPaperCore.module.anticheat.data.player.playerdata.PlayerData;
import com.github.retrooper.packetevents.event.PacketHandler;
import com.github.retrooper.packetevents.event.PacketListenerAbstract;
import com.github.retrooper.packetevents.event.PacketListenerPriority;

import com.github.retrooper.packetevents.event.PacketReceiveEvent;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.protocol.packettype.PacketTypeCommon;
import com.github.retrooper.packetevents.util.Vector3i;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientAttack;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientInteractEntity;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientPlayerDigging;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientPlayerFlying;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.Plugin;

import java.util.UUID;
import java.util.logging.Level;

public class AnticheatListener extends PacketListenerAbstract implements Listener {

    private final PlayerDataManager playerDataManager;

    private final AnticheatStateService stateService;

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

            default:
                return;
        }
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