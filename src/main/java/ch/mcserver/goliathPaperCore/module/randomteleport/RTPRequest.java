package ch.mcserver.goliathPaperCore.module.randomteleport;

import ch.mcserver.goliathPaperCore.GoliathPaperCore;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.util.UUID;

public class RTPRequest {

    private final UUID playerUuid;
    private final Region region;
    private final String sourceServer;
    private final World.Environment dimension;
    private final UUID requestUuid;

    public RTPRequest(Player player, Region region, World.Environment dimension) {
        this.playerUuid = player.getUniqueId();
        this.region = region;
        this.dimension = dimension;
        this.requestUuid = UUID.randomUUID();
        this.sourceServer = GoliathPaperCore.serverName;
    }

    public UUID getPlayerUuid() {
        return playerUuid;
    }

    public Region getRegion() {
        return region;
    }

    public World.Environment getDimension() {
        return dimension;
    }

    public UUID getRequestUuid() {
        return requestUuid;
    }
}