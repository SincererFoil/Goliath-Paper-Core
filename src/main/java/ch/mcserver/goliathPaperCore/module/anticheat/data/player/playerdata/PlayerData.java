package ch.mcserver.goliathPaperCore.module.anticheat.data.player.playerdata;

import ch.mcserver.goliathPaperCore.module.anticheat.checks.CheckManager;
import ch.mcserver.goliathPaperCore.module.anticheat.data.player.PacketData.*;
import ch.mcserver.goliathPaperCore.module.anticheat.flag.FlagManager;
import com.github.retrooper.packetevents.protocol.player.InteractionHand;
import com.github.retrooper.packetevents.util.Vector3i;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientClickWindow;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.*;

public class PlayerData {

    private UUID uuid;

    private String username;

    private int entityId;

    private GameMode gameMode;

    private boolean isOnline;

    private boolean isDead;

    private double x;

    private double y;

    private double z;

    private double lastX;

    private double lastY;

    private double lastZ;

    private double deltaX;

    private double deltaY;

    private double deltaZ;

    private double verticalDelta;

    private double horizontalDelta;

    private float yaw;

    private float pitch;

    private float lastYaw;

    private float lastPitch;

    private float deltaYaw;

    private float deltaPitch;

    private double lastDeltaYaw;

    private double lastDeltaPitch;

    private List<Float> yawHistory;

    private List<Float> pitchHistory;

    private List<Double> deltaYawHistory;

    private List<Double> deltaPitchHistory;

    private boolean onGround;

    private boolean lastOnGround;

    private boolean clientOnGround;

    private boolean serverOnGround;

    private boolean moving;

    private boolean rotating;

    private boolean sprinting;

    private boolean sneaking;

    private boolean swimming;

    private boolean gliding;

    private boolean flying;

    private boolean jumping;

    private boolean inVehicle;

    private int ticksSinceMove;

    private int ticksSinceRotation;

    private int ticksSinceGround;

    private int ticksSinceAir;

    private int airTicks;

    private int groundTicks;

    private Double fallDistance;

    private Double velocityX;

    private Double velocityY;

    private Double velocityZ;

    private Double lastVelocityX;

    private Double lastVelocityY;

    private Double lastVelocityZ;

    private int velocityTicks;

    private boolean hasVelocity;

    private boolean velocityPending;

    private int velocitySequence;

    private final Map<Integer, Integer> pendingVelocityPings = new HashMap<>();

    private boolean teleporting;

    private long lastTeleportTime;

    private int ticksSinceTeleport;

    private Double teleportX;

    private Double teleportY;

    private Double teleportZ;

    private int pendingTeleportId;

    private World world;

    private int worldId;

    private Block blockBelow;

    private Block blockAbove;

    private boolean insideBlock;

    private boolean insideLiquid;

    private boolean inWater;

    private boolean inLava;

    private boolean onIce;

    private boolean onSlime;

    private boolean onHoney;

    private boolean onSoulSand;

    private boolean nearWall;

    private boolean nearCeiling;

    private boolean climbing;

    private boolean onLadder;

    private boolean onVine;

    private long lastAttackTime;

    private int ticksSinceAttack;

    private String lastTarget;

    private int lastTargetEntityId;

    private int attackCount;

    private List<String> attackHistory;

    private long lastUseEntityTime;

    private long lastHitPosition;

    private int combatTicks;


    private int rotationSamples;

    private Double lastAimTarget;

    private Double aimAngle;

    private float targetYaw;

    private float targetPitch;

    private Double angleToTarget;

    private Double sensitivityEstimate;

    private Double mouseGcd;

    private Double rotationConsistency;


    private long lastBlockBreakTime;

    private long lastBlockPlaceTime;

    private long lastBlockInteractTime;

    private Vector3i lastBrokenBlock;

    private Vector3i  lastPlacedBlock;

    private Vector3i  lastInteractedBlock;

    private List<BlockBreakSample> blockBreakHistory;

    private List<Long> blockPlaceHistory;

    private Vector3i  diggingBlockPosition;

    private boolean digging;

    private boolean diggingBlock;

    private long digStartTime;

    private int digTicks;

    private DigAction lastDigAction;

    private boolean windowOpenClient;

    private boolean windowOpenServer;

    private Component windowTitle;

    private int windowIdServer;

    private int windowIdClient;

    private long lastWindowOpenTime;

    private long lastWindowCloseTime;

    private int ticksSinceWindowOpen;

    private int ticksSinceWindowClose;

    private int inventoryClicks;

    private long lastInventoryClickTime;

    private List<Long> inventoryClickHistory;

    private int lastClickedSlot;

    private WrapperPlayClientClickWindow.WindowClickType lastClickType;

    private com.github.retrooper.packetevents.protocol.item.ItemStack cursorItem;

    private int heldSlot;

    private int lastHeldSlot;

    private List<Integer> hotbarSlotChanges;

    private long lastSlotChangeTime;

    private int inventoryActionCount;


    private ItemStack heldItem;

    private ItemStack offHandItem;

    private ItemStack usingItem;

    private int useItemTicks;

    private long lastUseItemTime;

    private boolean blocking;

    private boolean eating;

    private boolean drinking;

    private boolean drawingBow;


    private int ping;

    private int lastPing;

    private int transactionPing;

    private int keepAlivePing;

    private int packetCount;

    private int movementPacketCount;

    private int rotationPacketCount;

    private int positionPacketCount;

    private long lastPacketTime;

    private int transactionsSent;

    private int transactionsReceived;

    private long lastTransactionTime;

    private int pendingTransactions;


    private long lastMovementPacket;

    private long lastPositionPacket;

    private long lastRotationPacket;

    private long lastFlyingPacket;

    private long lastUseEntityPacket;

    private long lastDigPacket;

    private long lastPlacePacket;

    private long lastWindowClickPacket;

    private long lastHeldItemChangePacket;


    private int currentTick;

    private int joinTick;

    private int ticksSinceJoin;

    private int lastMovementTick;

    private int lastRotationTick;

    private int lastAttackTick;

    private int lastDigTick;

    private int lastInventoryTick;


    private long lastAction;

    private long lastActionTime;

    private List<Long> actionHistory;

    private boolean interacting;

    private boolean placing;

    private boolean breaking;

    private boolean attacking;

    private boolean clickingInventory;


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

        hotbarSlotChanges = new ArrayList<>();
        inventoryClickHistory = new ArrayList<>();
        yawHistory = new ArrayList<>();
        pitchHistory = new ArrayList<>();
        deltaYawHistory = new ArrayList<>();
        deltaPitchHistory = new ArrayList<>();
        blockBreakHistory = new ArrayList<>();
        blockPlaceHistory = new ArrayList<>();
        attackHistory = new ArrayList<>();
        actionHistory = new ArrayList<>();
    }

    public UUID getUuid() {
        return uuid;
    }

    public Map<Integer, Integer> getPendingVelocityPings() {
        return pendingVelocityPings;
    }

    public int getPendingTeleportId() {
        return pendingTeleportId;
    }

    public void setPendingTeleportId(int pendingTeleportId) {
        this.pendingTeleportId = pendingTeleportId;
    }

    public int getVelocitySequence() {
        return velocitySequence;
    }

    public void setVelocitySequence(int velocitySequence) {
        this.velocitySequence = velocitySequence;
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

    public int getEntityId() {
        return entityId;
    }

    public void setEntityId(int entityId) {
        this.entityId = entityId;
    }

    public GameMode getGameMode() {
        return gameMode;
    }

    public void setGameMode(GameMode gameMode) {
        this.gameMode = gameMode;
    }

    public boolean isOnline() {
        return isOnline;
    }

    public void setOnline(boolean online) {
        isOnline = online;
    }

    public boolean isDead() {
        return isDead;
    }

    public void setDead(boolean dead) {
        isDead = dead;
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

    public double getDeltaZ() {
        return deltaZ;
    }

    public void setDeltaZ(double deltaZ) {
        this.deltaZ = deltaZ;
    }

    public double getVerticalDelta() {
        return verticalDelta;
    }

    public void setVerticalDelta(double verticalDelta) {
        this.verticalDelta = verticalDelta;
    }

    public double getHorizontalDelta() {
        return horizontalDelta;
    }

    public void setHorizontalDelta(double horizontalDelta) {
        this.horizontalDelta = horizontalDelta;
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

    public double getLastDeltaYaw() {
        return lastDeltaYaw;
    }

    public void setLastDeltaYaw(double lastDeltaYaw) {
        this.lastDeltaYaw = lastDeltaYaw;
    }

    public double getLastDeltaPitch() {
        return lastDeltaPitch;
    }

    public void setLastDeltaPitch(double lastDeltaPitch) {
        this.lastDeltaPitch = lastDeltaPitch;
    }

    public List<Float> getYawHistory() {
        return yawHistory;
    }

    public void setYawHistory(List<Float> yawHistory) {
        this.yawHistory = yawHistory;
    }

    public List<Float> getPitchHistory() {
        return pitchHistory;
    }

    public void setPitchHistory(List<Float> pitchHistory) {
        this.pitchHistory = pitchHistory;
    }

    public List<Double> getDeltaYawHistory() {
        return deltaYawHistory;
    }

    public void setDeltaYawHistory(List<Double> deltaYawHistory) {
        this.deltaYawHistory = deltaYawHistory;
    }

    public List<Double> getDeltaPitchHistory() {
        return deltaPitchHistory;
    }

    public void setDeltaPitchHistory(List<Double> deltaPitchHistory) {
        this.deltaPitchHistory = deltaPitchHistory;
    }

    public boolean isOnGround() {
        return onGround;
    }

    public void setOnGround(boolean onGround) {
        this.onGround = onGround;
    }

    public boolean isLastOnGround() {
        return lastOnGround;
    }

    public void setLastOnGround(boolean lastOnGround) {
        this.lastOnGround = lastOnGround;
    }

    public boolean isClientOnGround() {
        return clientOnGround;
    }

    public void setClientOnGround(boolean clientOnGround) {
        this.clientOnGround = clientOnGround;
    }

    public boolean isServerOnGround() {
        return serverOnGround;
    }

    public void setServerOnGround(boolean serverOnGround) {
        this.serverOnGround = serverOnGround;
    }

    public boolean isMoving() {
        return moving;
    }

    public void setMoving(boolean moving) {
        this.moving = moving;
    }

    public boolean isRotating() {
        return rotating;
    }

    public void setRotating(boolean rotating) {
        this.rotating = rotating;
    }

    public boolean isSprinting() {
        return sprinting;
    }

    public void setSprinting(boolean sprinting) {
        this.sprinting = sprinting;
    }

    public boolean isSneaking() {
        return sneaking;
    }

    public void setSneaking(boolean sneaking) {
        this.sneaking = sneaking;
    }

    public boolean isSwimming() {
        return swimming;
    }

    public void setSwimming(boolean swimming) {
        this.swimming = swimming;
    }

    public boolean isGliding() {
        return gliding;
    }

    public void setGliding(boolean gliding) {
        this.gliding = gliding;
    }

    public boolean isFlying() {
        return flying;
    }

    public void setFlying(boolean flying) {
        this.flying = flying;
    }

    public boolean isJumping() {
        return jumping;
    }

    public void setJumping(boolean jumping) {
        this.jumping = jumping;
    }

    public boolean isInVehicle() {
        return inVehicle;
    }

    public void setInVehicle(boolean inVehicle) {
        this.inVehicle = inVehicle;
    }

    public int getTicksSinceMove() {
        return ticksSinceMove;
    }

    public void setTicksSinceMove(int ticksSinceMove) {
        this.ticksSinceMove = ticksSinceMove;
    }

    public int getTicksSinceRotation() {
        return ticksSinceRotation;
    }

    public void setTicksSinceRotation(int ticksSinceRotation) {
        this.ticksSinceRotation = ticksSinceRotation;
    }

    public int getTicksSinceGround() {
        return ticksSinceGround;
    }

    public void setTicksSinceGround(int ticksSinceGround) {
        this.ticksSinceGround = ticksSinceGround;
    }

    public int getTicksSinceAir() {
        return ticksSinceAir;
    }

    public void setTicksSinceAir(int ticksSinceAir) {
        this.ticksSinceAir = ticksSinceAir;
    }

    public int getAirTicks() {
        return airTicks;
    }

    public void setAirTicks(int airTicks) {
        this.airTicks = airTicks;
    }

    public int getGroundTicks() {
        return groundTicks;
    }

    public void setGroundTicks(int groundTicks) {
        this.groundTicks = groundTicks;
    }

    public Double getFallDistance() {
        return fallDistance;
    }

    public void setFallDistance(Double fallDistance) {
        this.fallDistance = fallDistance;
    }

    public Double getVelocityX() {
        return velocityX;
    }

    public void setVelocityX(Double velocityX) {
        this.velocityX = velocityX;
    }

    public Double getVelocityY() {
        return velocityY;
    }

    public void setVelocityY(Double velocityY) {
        this.velocityY = velocityY;
    }

    public Double getVelocityZ() {
        return velocityZ;
    }

    public void setVelocityZ(Double velocityZ) {
        this.velocityZ = velocityZ;
    }

    public Double getLastVelocityX() {
        return lastVelocityX;
    }

    public void setLastVelocityX(Double lastVelocityX) {
        this.lastVelocityX = lastVelocityX;
    }

    public Double getLastVelocityY() {
        return lastVelocityY;
    }

    public void setLastVelocityY(Double lastVelocityY) {
        this.lastVelocityY = lastVelocityY;
    }

    public Double getLastVelocityZ() {
        return lastVelocityZ;
    }

    public void setLastVelocityZ(Double lastVelocityZ) {
        this.lastVelocityZ = lastVelocityZ;
    }

    public int getVelocityTicks() {
        return velocityTicks;
    }

    public void setVelocityTicks(int velocityTicks) {
        this.velocityTicks = velocityTicks;
    }

    public boolean isHasVelocity() {
        return hasVelocity;
    }

    public void setHasVelocity(boolean hasVelocity) {
        this.hasVelocity = hasVelocity;
    }

    public boolean isVelocityPending() {
        return velocityPending;
    }

    public void setVelocityPending(boolean velocityPending) {
        this.velocityPending = velocityPending;
    }

    public boolean isTeleporting() {
        return teleporting;
    }

    public void setTeleporting(boolean teleporting) {
        this.teleporting = teleporting;
    }

    public long getLastTeleportTime() {
        return lastTeleportTime;
    }

    public void setLastTeleportTime(long lastTeleportTime) {
        this.lastTeleportTime = lastTeleportTime;
    }

    public int getTicksSinceTeleport() {
        return ticksSinceTeleport;
    }

    public void setTicksSinceTeleport(int ticksSinceTeleport) {
        this.ticksSinceTeleport = ticksSinceTeleport;
    }

    public Double getTeleportX() {
        return teleportX;
    }

    public void setTeleportX(Double teleportX) {
        this.teleportX = teleportX;
    }

    public Double getTeleportY() {
        return teleportY;
    }

    public void setTeleportY(Double teleportY) {
        this.teleportY = teleportY;
    }

    public Double getTeleportZ() {
        return teleportZ;
    }

    public void setTeleportZ(Double teleportZ) {
        this.teleportZ = teleportZ;
    }

    public World getWorld() {
        return world;
    }

    public void setWorld(World world) {
        this.world = world;
    }

    public int getWorldId() {
        return worldId;
    }

    public void setWorldId(int worldId) {
        this.worldId = worldId;
    }

    public Block getBlockBelow() {
        return blockBelow;
    }

    public void setBlockBelow(Block blockBelow) {
        this.blockBelow = blockBelow;
    }

    public Block getBlockAbove() {
        return blockAbove;
    }

    public void setBlockAbove(Block blockAbove) {
        this.blockAbove = blockAbove;
    }

    public boolean isInsideBlock() {
        return insideBlock;
    }

    public void setInsideBlock(boolean insideBlock) {
        this.insideBlock = insideBlock;
    }

    public boolean isInsideLiquid() {
        return insideLiquid;
    }

    public void setInsideLiquid(boolean insideLiquid) {
        this.insideLiquid = insideLiquid;
    }

    public boolean isInWater() {
        return inWater;
    }

    public void setInWater(boolean inWater) {
        this.inWater = inWater;
    }

    public boolean isInLava() {
        return inLava;
    }

    public void setInLava(boolean inLava) {
        this.inLava = inLava;
    }

    public boolean isOnIce() {
        return onIce;
    }

    public void setOnIce(boolean onIce) {
        this.onIce = onIce;
    }

    public boolean isOnSlime() {
        return onSlime;
    }

    public void setOnSlime(boolean onSlime) {
        this.onSlime = onSlime;
    }

    public boolean isOnHoney() {
        return onHoney;
    }

    public void setOnHoney(boolean onHoney) {
        this.onHoney = onHoney;
    }

    public boolean isOnSoulSand() {
        return onSoulSand;
    }

    public void setOnSoulSand(boolean onSoulSand) {
        this.onSoulSand = onSoulSand;
    }

    public boolean isNearWall() {
        return nearWall;
    }

    public void setNearWall(boolean nearWall) {
        this.nearWall = nearWall;
    }

    public boolean isNearCeiling() {
        return nearCeiling;
    }

    public void setNearCeiling(boolean nearCeiling) {
        this.nearCeiling = nearCeiling;
    }

    public boolean isClimbing() {
        return climbing;
    }

    public void setClimbing(boolean climbing) {
        this.climbing = climbing;
    }

    public boolean isOnLadder() {
        return onLadder;
    }

    public void setOnLadder(boolean onLadder) {
        this.onLadder = onLadder;
    }

    public boolean isOnVine() {
        return onVine;
    }

    public void setOnVine(boolean onVine) {
        this.onVine = onVine;
    }

    public long getLastAttackTime() {
        return lastAttackTime;
    }

    public void setLastAttackTime(long lastAttackTime) {
        this.lastAttackTime = lastAttackTime;
    }

    public int getTicksSinceAttack() {
        return ticksSinceAttack;
    }

    public void setTicksSinceAttack(int ticksSinceAttack) {
        this.ticksSinceAttack = ticksSinceAttack;
    }

    public String getLastTarget() {
        return lastTarget;
    }

    public void setLastTarget(String lastTarget) {
        this.lastTarget = lastTarget;
    }

    public int getLastTargetEntityId() {
        return lastTargetEntityId;
    }

    public void setLastTargetEntityId(int lastTargetEntityId) {
        this.lastTargetEntityId = lastTargetEntityId;
    }

    public int getAttackCount() {
        return attackCount;
    }

    public void setAttackCount(int attackCount) {
        this.attackCount = attackCount;
    }

    public List<String> getAttackHistory() {
        return attackHistory;
    }

    public void setAttackHistory(List<String> attackHistory) {
        this.attackHistory = attackHistory;
    }

    public long getLastUseEntityTime() {
        return lastUseEntityTime;
    }

    public void setLastUseEntityTime(long lastUseEntityTime) {
        this.lastUseEntityTime = lastUseEntityTime;
    }

    public long getLastHitPosition() {
        return lastHitPosition;
    }

    public void setLastHitPosition(long lastHitPosition) {
        this.lastHitPosition = lastHitPosition;
    }

    public int getCombatTicks() {
        return combatTicks;
    }

    public void setCombatTicks(int combatTicks) {
        this.combatTicks = combatTicks;
    }

    public int getRotationSamples() {
        return rotationSamples;
    }

    public void setRotationSamples(int rotationSamples) {
        this.rotationSamples = rotationSamples;
    }

    public Double getLastAimTarget() {
        return lastAimTarget;
    }

    public void setLastAimTarget(Double lastAimTarget) {
        this.lastAimTarget = lastAimTarget;
    }

    public Double getAimAngle() {
        return aimAngle;
    }

    public void setAimAngle(Double aimAngle) {
        this.aimAngle = aimAngle;
    }

    public float getTargetYaw() {
        return targetYaw;
    }

    public void setTargetYaw(float targetYaw) {
        this.targetYaw = targetYaw;
    }

    public float getTargetPitch() {
        return targetPitch;
    }

    public void setTargetPitch(float targetPitch) {
        this.targetPitch = targetPitch;
    }

    public Double getAngleToTarget() {
        return angleToTarget;
    }

    public void setAngleToTarget(Double angleToTarget) {
        this.angleToTarget = angleToTarget;
    }

    public Double getSensitivityEstimate() {
        return sensitivityEstimate;
    }

    public void setSensitivityEstimate(Double sensitivityEstimate) {
        this.sensitivityEstimate = sensitivityEstimate;
    }

    public Double getMouseGcd() {
        return mouseGcd;
    }

    public void setMouseGcd(Double mouseGcd) {
        this.mouseGcd = mouseGcd;
    }

    public Double getRotationConsistency() {
        return rotationConsistency;
    }

    public void setRotationConsistency(Double rotationConsistency) {
        this.rotationConsistency = rotationConsistency;
    }

    public long getLastBlockBreakTime() {
        return lastBlockBreakTime;
    }

    public void setLastBlockBreakTime(long lastBlockBreakTime) {
        this.lastBlockBreakTime = lastBlockBreakTime;
    }

    public long getLastBlockPlaceTime() {
        return lastBlockPlaceTime;
    }

    public void setLastBlockPlaceTime(long lastBlockPlaceTime) {
        this.lastBlockPlaceTime = lastBlockPlaceTime;
    }

    public long getLastBlockInteractTime() {
        return lastBlockInteractTime;
    }

    public void setLastBlockInteractTime(long lastBlockInteractTime) {
        this.lastBlockInteractTime = lastBlockInteractTime;
    }

    public Vector3i getLastBrokenBlock() {
        return lastBrokenBlock;
    }

    public void setLastBrokenBlock(Vector3i lastBrokenBlock) {
        this.lastBrokenBlock = lastBrokenBlock;
    }

    public Vector3i getLastPlacedBlock() {
        return lastPlacedBlock;
    }

    public void setLastPlacedBlock(Vector3i lastPlacedBlock) {
        this.lastPlacedBlock = lastPlacedBlock;
    }

    public Vector3i getLastInteractedBlock() {
        return lastInteractedBlock;
    }

    public void setLastInteractedBlock(Vector3i lastInteractedBlock) {
        this.lastInteractedBlock = lastInteractedBlock;
    }

    public List<BlockBreakSample> getBlockBreakHistory() {
        return blockBreakHistory;
    }

    public void setBlockBreakHistory(List<BlockBreakSample> blockBreakHistory) {
        this.blockBreakHistory = blockBreakHistory;
    }

    public List<Long> getBlockPlaceHistory() {
        return blockPlaceHistory;
    }

    public void setBlockPlaceHistory(List<Long> blockPlaceHistory) {
        this.blockPlaceHistory = blockPlaceHistory;
    }

    public Vector3i getDiggingBlockPosition() {
        return diggingBlockPosition;
    }

    public void setDiggingBlockPosition(Vector3i diggingBlockPosition) {
        this.diggingBlockPosition = diggingBlockPosition;
    }

    public boolean isDigging() {
        return digging;
    }

    public void setDigging(boolean digging) {
        this.digging = digging;
    }

    public boolean isDiggingBlock() {
        return diggingBlock;
    }

    public void setDiggingBlock(boolean diggingBlock) {
        this.diggingBlock = diggingBlock;
    }

    public long getDigStartTime() {
        return digStartTime;
    }

    public void setDigStartTime(long digStartTime) {
        this.digStartTime = digStartTime;
    }

    public int getDigTicks() {
        return digTicks;
    }

    public void setDigTicks(int digTicks) {
        this.digTicks = digTicks;
    }

    public DigAction getLastDigAction() {
        return lastDigAction;
    }

    public void setLastDigAction(DigAction lastDigAction) {
        this.lastDigAction = lastDigAction;
    }

    public boolean isClientWindowOpen() {
        return windowOpenClient;
    }

    public void setClientWindowOpen(boolean clientWindowOpen) {
        this.windowOpenClient = clientWindowOpen;
    }

    public boolean isServerWindowOpen() {
        return windowOpenServer;
    }

    public void setServerWindowOpen(boolean serverWindowOpen) {
        this.windowOpenServer = serverWindowOpen;
    }

    public int getWindowIdClient() {
        return windowIdClient;
    }

    public void setWindowIdClient(int windowIdClient) {
        this.windowIdClient = windowIdClient;
    }

    public int getWindowIdServer() {
        return windowIdServer;
    }

    public void setWindowIdServer(int windowIdServer) {
        this.windowIdServer = windowIdServer;
    }

    public Component getWindowTitle() {
        return windowTitle;
    }

    public void setWindowTitle(Component windowTitle) {
        this.windowTitle = windowTitle;
    }

    public long getLastWindowOpenTime() {
        return lastWindowOpenTime;
    }

    public void setLastWindowOpenTime(long lastWindowOpenTime) {
        this.lastWindowOpenTime = lastWindowOpenTime;
    }

    public long getLastWindowCloseTime() {
        return lastWindowCloseTime;
    }

    public void setLastWindowCloseTime(long lastWindowCloseTime) {
        this.lastWindowCloseTime = lastWindowCloseTime;
    }

    public int getTicksSinceWindowOpen() {
        return ticksSinceWindowOpen;
    }

    public void setTicksSinceWindowOpen(int ticksSinceWindowOpen) {
        this.ticksSinceWindowOpen = ticksSinceWindowOpen;
    }

    public int getTicksSinceWindowClose() {
        return ticksSinceWindowClose;
    }

    public void setTicksSinceWindowClose(int ticksSinceWindowClose) {
        this.ticksSinceWindowClose = ticksSinceWindowClose;
    }

    public int getInventoryClicks() {
        return inventoryClicks;
    }

    public void setInventoryClicks(int inventoryClicks) {
        this.inventoryClicks = inventoryClicks;
    }

    public long getLastInventoryClickTime() {
        return lastInventoryClickTime;
    }

    public void setLastInventoryClickTime(long lastInventoryClickTime) {
        this.lastInventoryClickTime = lastInventoryClickTime;
    }

    public List<Long> getInventoryClickHistory() {
        return inventoryClickHistory;
    }

    public void setInventoryClickHistory(List<Long> inventoryClickHistory) {
        this.inventoryClickHistory = inventoryClickHistory;
    }

    public int getLastClickedSlot() {
        return lastClickedSlot;
    }

    public void setLastClickedSlot(int lastClickedSlot) {
        this.lastClickedSlot = lastClickedSlot;
    }

    public WrapperPlayClientClickWindow.WindowClickType getLastClickType() {
        return lastClickType;
    }

    public void setLastClickType(WrapperPlayClientClickWindow.WindowClickType lastClickType) {
        this.lastClickType = lastClickType;
    }

    public com.github.retrooper.packetevents.protocol.item.ItemStack getCursorItem() {
        return cursorItem;
    }

    public void setCursorItem(com.github.retrooper.packetevents.protocol.item.ItemStack cursorItem) {
        this.cursorItem = cursorItem;
    }

    public int getHeldSlot() {
        return heldSlot;
    }

    public void setHeldSlot(int heldSlot) {
        this.heldSlot = heldSlot;
    }

    public int getLastHeldSlot() {
        return lastHeldSlot;
    }

    public void setLastHeldSlot(int lastHeldSlot) {
        this.lastHeldSlot = lastHeldSlot;
    }

    public List<Integer> getHotbarSlotChanges() {
        return hotbarSlotChanges;
    }

    public void setHotbarSlotChanges(List<Integer> hotbarSlotChanges) {
        this.hotbarSlotChanges = hotbarSlotChanges;
    }

    public long getLastSlotChangeTime() {
        return lastSlotChangeTime;
    }

    public void setLastSlotChangeTime(long lastSlotChangeTime) {
        this.lastSlotChangeTime = lastSlotChangeTime;
    }

    public int getInventoryActionCount() {
        return inventoryActionCount;
    }

    public void setInventoryActionCount(int inventoryActionCount) {
        this.inventoryActionCount = inventoryActionCount;
    }

    public ItemStack getHeldItem() {
        return heldItem;
    }

    public void setHeldItem(ItemStack heldItem) {
        this.heldItem = heldItem;
    }

    public ItemStack getOffHandItem() {
        return offHandItem;
    }

    public void setOffHandItem(ItemStack offHandItem) {
        this.offHandItem = offHandItem;
    }

    public ItemStack getUsingItem() {
        return usingItem;
    }

    public void setUsingItem(ItemStack usingItem) {
        this.usingItem = usingItem;
    }

    public int getUseItemTicks() {
        return useItemTicks;
    }

    public void setUseItemTicks(int useItemTicks) {
        this.useItemTicks = useItemTicks;
    }

    public long getLastUseItemTime() {
        return lastUseItemTime;
    }

    public void setLastUseItemTime(long lastUseItemTime) {
        this.lastUseItemTime = lastUseItemTime;
    }

    public boolean isBlocking() {
        return blocking;
    }

    public void setBlocking(boolean blocking) {
        this.blocking = blocking;
    }

    public boolean isEating() {
        return eating;
    }

    public void setEating(boolean eating) {
        this.eating = eating;
    }

    public boolean isDrinking() {
        return drinking;
    }

    public void setDrinking(boolean drinking) {
        this.drinking = drinking;
    }

    public boolean isDrawingBow() {
        return drawingBow;
    }

    public void setDrawingBow(boolean drawingBow) {
        this.drawingBow = drawingBow;
    }

    public int getPing() {
        return ping;
    }

    public void setPing(int ping) {
        this.ping = ping;
    }

    public int getLastPing() {
        return lastPing;
    }

    public void setLastPing(int lastPing) {
        this.lastPing = lastPing;
    }

    public int getTransactionPing() {
        return transactionPing;
    }

    public void setTransactionPing(int transactionPing) {
        this.transactionPing = transactionPing;
    }

    public int getKeepAlivePing() {
        return keepAlivePing;
    }

    public void setKeepAlivePing(int keepAlivePing) {
        this.keepAlivePing = keepAlivePing;
    }

    public int getPacketCount() {
        return packetCount;
    }

    public void setPacketCount(int packetCount) {
        this.packetCount = packetCount;
    }

    public int getMovementPacketCount() {
        return movementPacketCount;
    }

    public void setMovementPacketCount(int movementPacketCount) {
        this.movementPacketCount = movementPacketCount;
    }

    public int getRotationPacketCount() {
        return rotationPacketCount;
    }

    public void setRotationPacketCount(int rotationPacketCount) {
        this.rotationPacketCount = rotationPacketCount;
    }

    public int getPositionPacketCount() {
        return positionPacketCount;
    }

    public void setPositionPacketCount(int positionPacketCount) {
        this.positionPacketCount = positionPacketCount;
    }

    public long getLastPacketTime() {
        return lastPacketTime;
    }

    public void setLastPacketTime(long lastPacketTime) {
        this.lastPacketTime = lastPacketTime;
    }

    public int getTransactionsSent() {
        return transactionsSent;
    }

    public void setTransactionsSent(int transactionsSent) {
        this.transactionsSent = transactionsSent;
    }

    public int getTransactionsReceived() {
        return transactionsReceived;
    }

    public void setTransactionsReceived(int transactionsReceived) {
        this.transactionsReceived = transactionsReceived;
    }

    public long getLastTransactionTime() {
        return lastTransactionTime;
    }

    public void setLastTransactionTime(long lastTransactionTime) {
        this.lastTransactionTime = lastTransactionTime;
    }

    public int getPendingTransactions() {
        return pendingTransactions;
    }

    public void setPendingTransactions(int pendingTransactions) {
        this.pendingTransactions = pendingTransactions;
    }

    public long getLastMovementPacket() {
        return lastMovementPacket;
    }

    public void setLastMovementPacket(long lastMovementPacket) {
        this.lastMovementPacket = lastMovementPacket;
    }

    public long getLastPositionPacket() {
        return lastPositionPacket;
    }

    public void setLastPositionPacket(long lastPositionPacket) {
        this.lastPositionPacket = lastPositionPacket;
    }

    public long getLastRotationPacket() {
        return lastRotationPacket;
    }

    public void setLastRotationPacket(long lastRotationPacket) {
        this.lastRotationPacket = lastRotationPacket;
    }

    public long getLastFlyingPacket() {
        return lastFlyingPacket;
    }

    public void setLastFlyingPacket(long lastFlyingPacket) {
        this.lastFlyingPacket = lastFlyingPacket;
    }

    public long getLastUseEntityPacket() {
        return lastUseEntityPacket;
    }

    public void setLastUseEntityPacket(long lastUseEntityPacket) {
        this.lastUseEntityPacket = lastUseEntityPacket;
    }

    public long getLastDigPacket() {
        return lastDigPacket;
    }

    public void setLastDigPacket(long lastDigPacket) {
        this.lastDigPacket = lastDigPacket;
    }

    public long getLastPlacePacket() {
        return lastPlacePacket;
    }

    public void setLastPlacePacket(long lastPlacePacket) {
        this.lastPlacePacket = lastPlacePacket;
    }

    public long getLastWindowClickPacket() {
        return lastWindowClickPacket;
    }

    public void setLastWindowClickPacket(long lastWindowClickPacket) {
        this.lastWindowClickPacket = lastWindowClickPacket;
    }

    public long getLastHeldItemChangePacket() {
        return lastHeldItemChangePacket;
    }

    public void setLastHeldItemChangePacket(long lastHeldItemChangePacket) {
        this.lastHeldItemChangePacket = lastHeldItemChangePacket;
    }

    public int getCurrentTick() {
        return currentTick;
    }

    public void setCurrentTick(int currentTick) {
        this.currentTick = currentTick;
    }

    public int getJoinTick() {
        return joinTick;
    }

    public void setJoinTick(int joinTick) {
        this.joinTick = joinTick;
    }

    public int getTicksSinceJoin() {
        return ticksSinceJoin;
    }

    public void setTicksSinceJoin(int ticksSinceJoin) {
        this.ticksSinceJoin = ticksSinceJoin;
    }

    public int getLastMovementTick() {
        return lastMovementTick;
    }

    public void setLastMovementTick(int lastMovementTick) {
        this.lastMovementTick = lastMovementTick;
    }

    public int getLastRotationTick() {
        return lastRotationTick;
    }

    public void setLastRotationTick(int lastRotationTick) {
        this.lastRotationTick = lastRotationTick;
    }

    public int getLastAttackTick() {
        return lastAttackTick;
    }

    public void setLastAttackTick(int lastAttackTick) {
        this.lastAttackTick = lastAttackTick;
    }

    public int getLastDigTick() {
        return lastDigTick;
    }

    public void setLastDigTick(int lastDigTick) {
        this.lastDigTick = lastDigTick;
    }

    public int getLastInventoryTick() {
        return lastInventoryTick;
    }

    public void setLastInventoryTick(int lastInventoryTick) {
        this.lastInventoryTick = lastInventoryTick;
    }

    public long getLastAction() {
        return lastAction;
    }

    public void setLastAction(long lastAction) {
        this.lastAction = lastAction;
    }

    public long getLastActionTime() {
        return lastActionTime;
    }

    public void setLastActionTime(long lastActionTime) {
        this.lastActionTime = lastActionTime;
    }

    public List<Long> getActionHistory() {
        return actionHistory;
    }

    public void setActionHistory(List<Long> actionHistory) {
        this.actionHistory = actionHistory;
    }

    public boolean isInteracting() {
        return interacting;
    }

    public void setInteracting(boolean interacting) {
        this.interacting = interacting;
    }

    public boolean isPlacing() {
        return placing;
    }

    public void setPlacing(boolean placing) {
        this.placing = placing;
    }

    public boolean isBreaking() {
        return breaking;
    }

    public void setBreaking(boolean breaking) {
        this.breaking = breaking;
    }

    public boolean isAttacking() {
        return attacking;
    }

    public void setAttacking(boolean attacking) {
        this.attacking = attacking;
    }

    public boolean isClickingInventory() {
        return clickingInventory;
    }

    public void setClickingInventory(boolean clickingInventory) {
        this.clickingInventory = clickingInventory;
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

    public void tick() {
        ticksSinceTeleport++;
        ticksSinceAttack++;
        ticksSinceMove++;
        ticksSinceRotation++;
        ticksSinceWindowOpen++;
        ticksSinceWindowClose++;
        ticksSinceJoin++;

        if (clientOnGround) {
            groundTicks++;
            airTicks = 0;
            ticksSinceGround = 0;
            ticksSinceAir++;
        } else {
            airTicks++;
            groundTicks = 0;
            ticksSinceAir = 0;
            ticksSinceGround++;
        }

        if (usingItem != null) {
            useItemTicks++;
        }

        if (digging) {
            digTicks++;
        }

        if (hasVelocity) {
            velocityTicks++;
        }


        interacting = false;
        clickingInventory = false;

    }


    public void updateVelocity(VelocityPacketData data) {
        lastVelocityX = velocityX;
        lastVelocityY = velocityY;
        lastVelocityZ = velocityZ;

        velocityX = data.velocityX();
        velocityY = data.velocityY();
        velocityZ = data.velocityZ();

        velocityTicks = 0;

        velocitySequence++;

        hasVelocity = true;
        velocityPending = true;
    }

    public void acknowledgeVelocityPing(int pingId) {
        Integer sequence = pendingVelocityPings.remove(pingId);

        if (sequence == null) {
            return;
        }

        if (sequence == velocitySequence) {
            velocityPending = false;
        }
    }

    public void registerVelocityPing(int pingId) {
        pendingVelocityPings.put(pingId, velocitySequence);
    }

    public void updatePlayerAction(PlayerActionPacketData data) {
        switch (data.action()) {
            case START_SPRINTING:
                sprinting = true;
                break;

            case STOP_SPRINTING:
                sprinting = false;
                break;

            case START_SNEAKING:
                sneaking = true;
                break;

            case STOP_SNEAKING:
                sneaking = false;
                break;

            case START_FLYING_WITH_ELYTRA:
                gliding = true;
                break;
        }
        lastActionTime = data.receivedAt();
    }

    public void updateUseItem(UseItemPacketData data) {
        lastUseItemTime = data.receivedAt();
        lastActionTime = data.receivedAt();
        useItemTicks = 0;
        usingItem  = data.hand() == InteractionHand.MAIN_HAND ? heldItem : offHandItem;
    }

    public void releaseUseItem() {
        usingItem = null;
        useItemTicks = 0;
        blocking = false;
        eating = false;
        drinking = false;
        drawingBow = false;
    }


    public void acknowledgeClientTeleport(int teleportId) {
        if (teleporting && pendingTeleportId == teleportId) {
            positionInitialized = false;
            rotationInitialized = false;
            teleporting = false;
        }
    }

    public void updateTeleport(TeleportPacketData data) {
        teleporting = true;
        teleportX = data.x();
        teleportY = data.y();
        teleportZ = data.z();
        pendingTeleportId = data.teleportId();

        lastTeleportTime = data.receivedAt();
        ticksSinceTeleport= 0;
    }

    public void updateRotation(RotationPacketData data) {
        rotationPacketCount++;
        lastRotationPacket = System.nanoTime();
        lastRotationTick = Bukkit.getCurrentTick();

        lastDeltaYaw = deltaYaw;
        lastDeltaPitch = deltaPitch;

        if (!rotationInitialized) {
            yaw = lastYaw = data.yaw();
            pitch = lastPitch = data.pitch();

            deltaYaw = 0.0F;
            deltaPitch = 0.0F;
            rotationInitialized = true;
        } else {
            lastYaw = yaw;
            lastPitch = pitch;

            yaw = data.yaw();
            pitch = data.pitch();

            double difference = ((double) yaw - lastYaw) % 360.0;

            if (difference >= 180.0) {
                difference -= 360.0;
            } else if (difference < -180.0) {
                difference += 360.0;
            }

            deltaYaw = (float) difference;
            deltaPitch = pitch - lastPitch;
        }

        rotating = deltaYaw != 0.0F || deltaPitch != 0.0F;

        if (rotating) {
            ticksSinceRotation = 0;
            rotationSamples++;
        }

    }

    public void updateMovement(MovementPacketData data) {
        long now = System.nanoTime();

        lastMovementPacket = now;
        movementPacketCount++;
        lastMovementTick = Bukkit.getCurrentTick();

        deltaX = 0.0;
        deltaY = 0.0;
        deltaZ = 0.0;
        horizontalDelta = 0.0;
        verticalDelta = 0.0;

        boolean hadPosition = positionInitialized;

        if (data.hasPosition()) {
            lastPositionPacket = now;
            positionPacketCount++;

            if (!positionInitialized) {
                x = lastX = data.x();
                y = lastY = data.y();
                z = lastZ = data.z();

                positionInitialized = true;
            } else {
                lastX = x;
                lastY = y;
                lastZ = z;

                x = data.x();
                y = data.y();
                z = data.z();

                deltaX = x - lastX;
                deltaY = y - lastY;
                deltaZ = z - lastZ;

                verticalDelta = deltaY;
                horizontalDelta = Math.hypot(deltaX, deltaZ);
            }
        }

        moving = horizontalDelta > 0.0 || verticalDelta != 0.0;

        lastOnGround = onGround;
        clientOnGround = data.onGround();
        onGround = clientOnGround;

        jumping = hadPosition && data.hasPosition() && lastOnGround && !onGround && deltaY > 0.0;

        if (fallDistance == null) {
            fallDistance = 0.0;
        }

        if (onGround) {
            fallDistance = 0.0;
        } else if (data.hasPosition() && deltaY < 0.0) {
            fallDistance += -deltaY;
        }

        if (moving) {
            ticksSinceMove = 0;
        }

    }

    public void updateAttack(AttackPacketData data) {
        lastAttackTick = Bukkit.getCurrentTick();
        attackCount++;
        lastAttackTime = data.receivedAt();
        ticksSinceAttack = 0;

        lastTarget = data.targetName();

        lastTargetEntityId = data.targetEntityId();


        attackHistory.add(data.targetName());
        if (attackHistory.size() > 200) {
            attackHistory.removeFirst();
        }
    }

    public void updateDig(DigPacketData data) {
        lastDigAction = data.action();
        lastDigPacket = data.receivedAt();

        switch (data.action()) {
            case START_DESTROY_BLOCK:
                digging = true;
                diggingBlock = true;
                breaking = true;
                digStartTime = data.receivedAt();
                digTicks = 0;
                lastDigTick = Bukkit.getCurrentTick();
                lastActionTime = data.receivedAt();
                diggingBlockPosition = new Vector3i(data.blockX(), data.blockY(), data.blockZ());
                break;
            case STOP_DESTROY_BLOCK, ABORT_DESTROY_BLOCK:
                digging = false;
                diggingBlock = false;
                breaking = false;
                lastDigTick = Bukkit.getCurrentTick();
                lastActionTime = data.receivedAt();
                break;
        }

    }

    public void updateHeldSlot(HeldSlotPacketData data) {
        lastHeldSlot = heldSlot;
        heldSlot = data.slot();

        lastSlotChangeTime = data.receivedAt();

        lastHeldItemChangePacket = data.receivedAt();

        hotbarSlotChanges.add(data.slot());
        if (hotbarSlotChanges.size() > 300) {
            hotbarSlotChanges.removeFirst();
        }
    }

    public void updateBlockInteraction(BlockInteractPacketData data) {
        interacting = true;

        lastBlockInteractTime = data.receivedAt();
        lastActionTime = data.receivedAt();

        lastInteractedBlock = new Vector3i(data.blockX(), data.blockY(), data.blockZ());
    }

    public void updateInventoryClick(InventoryClickPacketData data) {
        inventoryClicks++;
        inventoryActionCount++;

        lastInventoryClickTime = data.receivedAt();
        lastClickedSlot = data.slot();
        lastClickType = data.clickType();
        cursorItem = data.cursorItem();

        clickingInventory = true;

        lastWindowClickPacket = data.receivedAt();

        lastInventoryTick = Bukkit.getCurrentTick();

        inventoryClickHistory.add(data.receivedAt());
        if (inventoryClickHistory.size() > 1000) {
            inventoryClickHistory.removeFirst();
        }
    }

    public void updateWindowServer(WindowPacketData data) {
        windowOpenServer = data.open();
        windowTitle = data.windowTitle();
        clickingInventory = false;
        windowIdServer = data.windowId();

        if (windowOpenServer) {
            windowOpenClient = true;
            windowIdClient = data.windowId();
        }

        if (data.open()) {
            lastWindowOpenTime = data.receivedAt();
            ticksSinceWindowOpen = 0;

            inventoryActionCount = 0;
            inventoryClicks = 0;
        } else {
            lastWindowCloseTime = data.receivedAt();
            ticksSinceWindowClose = 0;
        }
    }

    public void updateCloseWindowClient(WindowPacketData data) {
        windowOpenClient = data.open();
        clickingInventory = false;
        windowIdClient = data.windowId();
    }


}

