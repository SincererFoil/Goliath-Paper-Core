package ch.mcserver.goliathPaperCore.common.database.mysql;

import java.sql.Timestamp;
import java.util.UUID;

public class PlayerLocationObject {

    private UUID uuid;
    private Timestamp timestamp;
    private float yaw;
    private float pitch;
    private int x;
    private int y;
    private int z;
    private String server;

    public PlayerLocationObject(UUID uuid, Timestamp timestamp, float yaw, float pitch, int x, int y, int z, String server) {
        this.uuid = uuid;
        this.timestamp = timestamp;
        this.yaw = yaw;
        this.pitch = pitch;
        this.x = x;
        this.y = y;
        this.z = z;
        this.server = server;
    }

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    public float getYaw() {
        return yaw;
    }

    public void setYaw(float yaw) {
        this.yaw = yaw;
    }

    public float getPitch() {
        return pitch;
    }

    public void setPitch(float pitch) {
        this.pitch = pitch;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getZ() {
        return z;
    }

    public void setZ(int z) {
        this.z = z;
    }

    public String getServer() {
        return server;
    }

    public void setServer(String server) {
        this.server = server;
    }
}
