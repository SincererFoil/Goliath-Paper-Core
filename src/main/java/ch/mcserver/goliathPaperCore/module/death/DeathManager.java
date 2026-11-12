package ch.mcserver.goliathPaperCore.module.death;

import ch.mcserver.goliathPaperCore.GoliathPaperCore;
import ch.mcserver.goliathPaperCore.module.randomteleport.RTPRequest;
import ch.mcserver.goliathPaperCore.module.randomteleport.RandomTeleportService;
import ch.mcserver.goliathPaperCore.module.randomteleport.Region;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerRespawnEvent;

public class DeathManager implements Listener {

    private RandomTeleportService service;

    public DeathManager(RandomTeleportService service) {
        this.service = service;
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        Component deathMessage = event.deathMessage();

        if (deathMessage == null) return;

        event.deathMessage(Component.text("💀 ", NamedTextColor.RED).append(deathMessage));
    }

    @EventHandler
    public void onPlayerRespawn(PlayerRespawnEvent event) {
        Player player = event.getPlayer();

        Region region = Region.valueOf(GoliathPaperCore.config.node("world-system", "region").getString("NA_EAST"));

        RTPRequest request = new RTPRequest(player, region, World.Environment.NORMAL);

        service.executeRandomTeleport(request);
    }
}
