package ch.mcserver.goliathPaperCore.module.sector;


import org.bukkit.World;

public class Sector {

    private final String serverName;

    private final int sectorX;

    private final int sectorZ;

    private World.Environment dimension;

    public Sector(String serverName, int sectorX, int sectorZ, World.Environment dimension) {
        this.serverName = serverName;
        this.sectorX = sectorX;
        this.sectorZ = sectorZ;
        this.dimension = dimension;
    }

    public String getServerName() {
        return serverName;
    }

    public int getSectorX() {
        return sectorX;
    }

    public int getSectorZ() {
        return sectorZ;
    }

    public World.Environment getDimension() {
        return dimension;
    }

    public void setDimension(World.Environment dimension) {
        this.dimension = dimension;
    }

}
