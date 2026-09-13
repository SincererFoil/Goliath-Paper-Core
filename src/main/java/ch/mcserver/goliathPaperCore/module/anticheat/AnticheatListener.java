package ch.mcserver.goliathPaperCore.module.anticheat;


import ch.mcserver.goliathPaperCore.module.anticheat.data.AnticheatStateService;
import ch.mcserver.goliathPaperCore.module.anticheat.data.MovementPacketData;
import ch.mcserver.goliathPaperCore.module.anticheat.data.PlayerData;
import ch.mcserver.goliathPaperCore.module.anticheat.data.PlayerDataManager;
import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.*;
import org.bukkit.Bukkit;
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
        super(plugin, ListenerPriority.HIGH, PacketType.Play.Client.POSITION, PacketType.Play.Client.POSITION_LOOK, PacketType.Play.Client.LOOK);
        this.playerDataManager = playerDataManager;
        this.stateService = stateService;
    }

    @Override
    public void onPacketReceiving(PacketEvent event) {
        PacketType type = event.getPacketType();

        boolean hasPosition = type.equals(PacketType.Play.Client.POSITION) || type.equals(PacketType.Play.Client.POSITION_LOOK);

        boolean hasRotation = type.equals(PacketType.Play.Client.LOOK) || type.equals(PacketType.Play.Client.POSITION_LOOK);

        if (!hasPosition && !hasRotation) {
            return;
        }

        double x = hasPosition ? event.getPacket().getDoubles().read(0) : 0;
        double y = hasPosition ? event.getPacket().getDoubles().read(1) : 0;
        double z = hasPosition ? event.getPacket().getDoubles().read(2) : 0;

        float yaw = hasRotation ? event.getPacket().getFloat().read(0) : 0;
        float pitch = hasRotation ? event.getPacket().getFloat().read(1) : 0;

        MovementPacketData data = new MovementPacketData(
                event.getPlayer().getUniqueId(),
                type,
                yaw,
                pitch,
                x,
                y,
                z,
                hasPosition,
                hasRotation
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

    @Override
    public void onPacketSending(PacketEvent event) {
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        PlayerData playerData = playerDataManager.create(event.getPlayer().getUniqueId(), event.getPlayer().getName());
        stateService.load(playerData);
    }

    @EventHandler
    public  void onQuit(PlayerQuitEvent event) {
        UUID uuid = event.getPlayer().getUniqueId();
        PlayerData playerData = playerDataManager.get(uuid);

        if (playerData != null&& playerData.isCheckStateLoaded()) {
            stateService.save(playerData);
        }
        playerDataManager.remove(event.getPlayer().getUniqueId());
    }

}
