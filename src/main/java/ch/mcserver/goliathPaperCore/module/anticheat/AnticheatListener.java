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


}