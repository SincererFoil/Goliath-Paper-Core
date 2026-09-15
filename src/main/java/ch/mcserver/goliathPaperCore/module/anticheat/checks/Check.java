package ch.mcserver.goliathPaperCore.module.anticheat.checks;

import ch.mcserver.goliathPaperCore.module.anticheat.data.CheckState;
import ch.mcserver.goliathPaperCore.module.anticheat.data.PacketData;
import ch.mcserver.goliathPaperCore.module.anticheat.data.PlayerData;
import ch.mcserver.goliathPaperCore.module.anticheat.flag.FlagManager;
import ch.mcserver.goliathPaperCore.module.anticheat.flag.FlagType;
import com.comphenix.protocol.PacketType;

import java.util.Set;
import java.util.concurrent.TimeUnit;

public abstract class Check {

    protected final PlayerData playerData;
    protected int violations;
    protected double buffer;

    protected long lastViolationUpdateAt;


    private final FlagType flagType;
    private final FlagManager flagManager;

    protected Check(PlayerData playerData, FlagType flagType, FlagManager flagManager) {
        this.playerData = playerData;
        this.flagType = flagType;
        this.flagManager = flagManager;
    }

    public abstract void handle(PacketData event);

    public abstract Set<PacketType> getPacketTypes();

    protected void flag(String details) {
        decayViolations();
        violations++;


        lastViolationUpdateAt = System.currentTimeMillis();

        flagManager.handleFlag(
                playerData,
                flagType,
                violations,
                details
        );
    }

    public CheckState createState() {
        decayViolations();
        return new CheckState(playerData.getUuid(), flagType.name(), violations, buffer, lastViolationUpdateAt);
    }

    public void applyState(CheckState state) {
        this.violations = state.violations();
        this.buffer = state.buffer();
        this.lastViolationUpdateAt = state.lastViolationUpdateAt();
        decayViolations();

    }

    protected void decayViolations() {

        if (lastViolationUpdateAt == 0) {
            lastViolationUpdateAt = System.currentTimeMillis();
            return;
        }

        if (violations <= 0) {
            return;
        }

        long timeSinceViolation = System.currentTimeMillis() - lastViolationUpdateAt;

        long decaySteps = timeSinceViolation / TimeUnit.MINUTES.toMillis(30);

        if (decaySteps <= 0) {
            return;
        }

        violations = Math.max(0, violations - (int) decaySteps);
        lastViolationUpdateAt += decaySteps * TimeUnit.MINUTES.toMillis(30);

    }


    public int getViolations() {
        return violations;
    }

    public double getBuffer() {
        return buffer;
    }

    public FlagType getFlagType() {
        return flagType;
    }

}