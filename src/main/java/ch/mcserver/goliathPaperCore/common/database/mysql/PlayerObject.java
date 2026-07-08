package ch.mcserver.goliathPaperCore.common.database.mysql;

import java.util.UUID;

public class PlayerObject {

    private UUID uuid;

    private String name;
    private String prefix;

    private String currentServer;

    private boolean sfmode;
    private boolean debugMode;
    private boolean gmsp;

    private float flySpeed;

    private long firstJoin;
    private long lastJoin;
    private boolean creative;

    public PlayerObject(
            UUID uuid,
            String name,
            String prefix,
            String currentServer,
            boolean sfmode,
            boolean debugMode,
            boolean gmsp,
            float flySpeed,
            long firstJoin,
            long lastJoin,
            boolean creative
    ) {

        this.uuid = uuid;
        this.name = name;
        this.prefix = prefix;

        this.currentServer = currentServer;

        this.sfmode = sfmode;
        this.debugMode = debugMode;
        this.gmsp = gmsp;

        this.flySpeed = flySpeed;

        this.firstJoin = firstJoin;
        this.lastJoin = lastJoin;
        this.creative = creative;
    }

    public UUID getUuid() {
        return uuid;
    }

    public String getName() {
        return name;
    }

    public String getPrefix() {
        return prefix;
    }

    public String getCurrentServer() {
        return currentServer;
    }

    public boolean isSfmode() {
        return sfmode;
    }

    public boolean isDebugMode() {
        return debugMode;
    }

    public boolean isGmsp() {
        return gmsp;
    }

    public float getFlySpeed() {
        return flySpeed;
    }

    public long getFirstJoin() {
        return firstJoin;
    }

    public long getLastJoin() {
        return lastJoin;
    }

    public boolean isCreative() {
        return creative;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public void setCurrentServer(String currentServer) {
        this.currentServer = currentServer;
    }

    public void setSfmode(boolean sfmode) {
        this.sfmode = sfmode;
    }

    public void setDebugMode(boolean debugMode) {
        this.debugMode = debugMode;
    }

    public void setGmsp(boolean gmsp) {
        this.gmsp = gmsp;
    }

    public void setFlySpeed(float flySpeed) {
        this.flySpeed = flySpeed;
    }

    public void setFirstJoin(long firstJoin) {
        this.firstJoin = firstJoin;
    }

    public void setLastJoin(long lastJoin) {
        this.lastJoin = lastJoin;
    }

    public void setCreative(boolean creative) {
        this.creative = creative;
    }
}
