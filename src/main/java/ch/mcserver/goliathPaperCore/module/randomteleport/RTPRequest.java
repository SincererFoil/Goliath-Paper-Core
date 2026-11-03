package ch.mcserver.goliathPaperCore.module.randomteleport;

import org.bukkit.World;
import org.bukkit.entity.Player;

import java.util.UUID;

public class RTPRequest {

    private Player player;

    private Region region;

    private World.Environment dimension;

    private UUID requestUuid;

    public RTPRequest(Player player, Region region, World.Environment dimension) {
        this.player = player;
        this.region = region;
        this.dimension = dimension;
        this.requestUuid = UUID.randomUUID();
    }

    public Player getPlayer() {
        return player;
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
