package ch.mcserver.goliathPaperCore.module.anticheat.modguard;

import com.github.retrooper.packetevents.event.*;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.util.Vector3i;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientUpdateSign;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.Plugin;


public class ModGuardPacketListener extends PacketListenerAbstract implements Listener {

    private final ModGuardScanService modGardScanService;

    private final Plugin plugin;

    public ModGuardPacketListener(ModGuardScanService modGardScanService, Plugin plugin) {
        this.modGardScanService = modGardScanService;
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        modGardScanService.startScan(event.getPlayer());
    }

    @PacketHandler
    public void onPacketReceive(PacketReceiveEvent event) {

        if (event.getPacketType() == PacketType.Play.Client.UPDATE_SIGN) {
            WrapperPlayClientUpdateSign packet = new WrapperPlayClientUpdateSign(event);

            if (!(event.getPlayer() instanceof Player player)) {
                return;
            }
            Vector3i position = packet.getBlockPosition();
            String[] lines = packet.getTextLines();

            Bukkit.getScheduler().runTask(plugin, () -> {
                modGardScanService.handleSignResponse(player, position, lines);
            });
        }

    }


}
