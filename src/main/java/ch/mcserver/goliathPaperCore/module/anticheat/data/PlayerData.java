package ch.mcserver.goliathPaperCore.module.anticheat.data;

import ch.mcserver.goliathPaperCore.module.anticheat.checks.CheckManager;
import ch.mcserver.goliathPaperCore.module.anticheat.flag.FlagManager;

import java.util.UUID;

public class PlayerData {

    private UUID uuid;

    private String username;

    private double x;

    private double y;

    private double z;

    private float pitch;

    private float yaw;

    private double lastX;

    private double lastY;

    private double lastZ;

    private float lastYaw;

    private float lastPitch;

    private double deltaX;

    private double deltaY;

    private double getDeltaZ;

    private float deltaYaw;

    private float deltaPitch;

    private final CheckManager checkManager;

    private boolean positionInitialized;

    private boolean rotationInitialized;

    private boolean checkStateLoaded;

    public PlayerData(UUID uuid, String username, FlagManager flagManager) {
        this.username = username;
        this.uuid = uuid;
        this.checkManager = new CheckManager(this, flagManager);
        positionInitialized = false;
        rotationInitialized = false;
    }

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public double getZ() {
        return z;
    }

    public void setZ(double z) {
        this.z = z;
    }

    public float getPitch() {
        return pitch;
    }

    public void setPitch(float pitch) {
        this.pitch = pitch;
    }

    public float getYaw() {
        return yaw;
    }

    public void setYaw(float yaw) {
        this.yaw = yaw;
    }

    public double getLastX() {
        return lastX;
    }

    public void setLastX(double lastX) {
        this.lastX = lastX;
    }

    public double getLastY() {
        return lastY;
    }

    public void setLastY(double lastY) {
        this.lastY = lastY;
    }

    public double getLastZ() {
        return lastZ;
    }

    public void setLastZ(double lastZ) {
        this.lastZ = lastZ;
    }

    public float getLastYaw() {
        return lastYaw;
    }

    public void setLastYaw(float lastYaw) {
        this.lastYaw = lastYaw;
    }

    public float getLastPitch() {
        return lastPitch;
    }

    public void setLastPitch(float lastPitch) {
        this.lastPitch = lastPitch;
    }

    public double getDeltaX() {
        return deltaX;
    }

    public void setDeltaX(double deltaX) {
        this.deltaX = deltaX;
    }

    public double getDeltaY() {
        return deltaY;
    }

    public void setDeltaY(double deltaY) {
        this.deltaY = deltaY;
    }

    public double getGetDeltaZ() {
        return getDeltaZ;
    }

    public void setDeltaZ(double getDeltaZ) {
        this.getDeltaZ = getDeltaZ;
    }

    public float getDeltaYaw() {
        return deltaYaw;
    }

    public void setDeltaYaw(float deltaYaw) {
        this.deltaYaw = deltaYaw;
    }

    public float getDeltaPitch() {
        return deltaPitch;
    }

    public void setDeltaPitch(float deltaPitch) {
        this.deltaPitch = deltaPitch;
    }

    public CheckManager getCheckManager() {
        return checkManager;
    }

    public boolean isPositionInitialized() {
        return positionInitialized;
    }

    public void setPositionInitialized(boolean positionInitialized) {
        this.positionInitialized = positionInitialized;
    }

    public boolean isRotationInitialized() {
        return rotationInitialized;
    }

    public void setRotationInitialized(boolean rotationInitialized) {
        this.rotationInitialized = rotationInitialized;
    }

    public boolean isCheckStateLoaded() {
        return checkStateLoaded;
    }

    public void setCheckStateLoaded(boolean checkStateLoaded) {
        this.checkStateLoaded = checkStateLoaded;
    }

    public void updateMovement(MovementPacketData data) {
        if (data.hasPosition()) {
            if (!positionInitialized) {
                setLastX(data.x());
                setLastY(data.y());
                setLastZ(data.z());
                positionInitialized = true;
            } else {
                setLastX(getX());
                setLastY(getY());
                setLastZ(getZ());
            }

            setX(data.x());
            setY(data.y());
            setZ(data.z());

            setDeltaX(getX() - getLastX());
            setDeltaY(getY() - getLastY());
            setDeltaZ(getZ() - getLastZ());

        }

        if (data.hasRotation()) {
            if (!rotationInitialized) {
                setLastPitch(data.pitch());
                setLastYaw(data.yaw());
                rotationInitialized = true;
            } else {
                setLastPitch(getPitch());
                setLastYaw(getYaw());
            }

            setPitch(data.pitch());
            setYaw(data.yaw());

            setDeltaPitch(getPitch() - getLastPitch());
            setDeltaYaw(getYaw() - getLastYaw());

            if (getDeltaYaw() > 180) {
                setDeltaYaw(getDeltaYaw() - 360);
            } else if (getDeltaYaw() < -180) {
                setDeltaYaw(getDeltaYaw() + 360);
            }
        }
    }
}
