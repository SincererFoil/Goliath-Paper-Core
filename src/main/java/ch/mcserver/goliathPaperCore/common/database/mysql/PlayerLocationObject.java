package ch.mcserver.goliathPaperCore.common.database.mysql;

import java.sql.Timestamp;
import java.util.UUID;

public class PlayerLocationObject {
    private UUID uuid;
    private Timestamp timestamp;
    private float Yaw;
    private float Pitch;
    private int x;
    private  int y;
    private  int z;
    private String server;

    public PlayerLocationObject(UUID uuid, Timestamp timestamp, float yaw, float pitch, int x, int y, int z, String server) {
        this.uuid = uuid;
        this.timestamp = timestamp;
        Yaw = yaw;
        Pitch = pitch;
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
        return Yaw;
    }

    public void setYaw(float yaw) {
        Yaw = yaw;
    }

    public float getPitch() {
        return Pitch;
    }

    public void setPitch(float pitch) {
        Pitch = pitch;
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

    public  void setServer(String server) {
        this.server = server;
    }

}
