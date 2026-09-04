package ch.mcserver.goliathPaperCore.module.anticheat;


import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.*;
import org.bukkit.plugin.Plugin;

public class AnticheatListener extends PacketAdapter {

    public AnticheatListener(Plugin plugin) {
        super(
                plugin,
                ListenerPriority.HIGH,
                PacketType.Play.Client.POSITION,
                PacketType.Play.Client.POSITION_LOOK,
                PacketType.Play.Client.LOOK
        );
    }

    @Override
    public void onPacketReceiving(PacketEvent event) {
    }

    @Override
    public void onPacketSending(PacketEvent event) {
    }

}
