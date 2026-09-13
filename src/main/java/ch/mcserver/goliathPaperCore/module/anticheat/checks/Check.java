package ch.mcserver.goliathPaperCore.module.anticheat.checks;

import ch.mcserver.goliathPaperCore.module.anticheat.data.CheckState;
import ch.mcserver.goliathPaperCore.module.anticheat.data.PacketData;
import ch.mcserver.goliathPaperCore.module.anticheat.data.PlayerData;
import ch.mcserver.goliathPaperCore.module.anticheat.flag.FlagManager;
import ch.mcserver.goliathPaperCore.module.anticheat.flag.FlagType;
import com.comphenix.protocol.PacketType;

import java.util.Set;

public abstract class Check {

    protected final PlayerData playerData;
    protected int violations;
    protected double buffer;
    private final FlagType flagType;

    protected Check(PlayerData playerData, FlagType flagType) {
        this.playerData = playerData;
        this.flagType = flagType;
    }

    public abstract void handle(PacketData event);

    public abstract Set<PacketType> getPacketTypes();

    protected void flag(String details) {
        violations++;

        FlagManager.handleFlag(
                playerData,
                flagType,
                violations,
                details
        );
    }

    public CheckState createState() {
        return new CheckState(playerData.getUuid(), flagType.name(), violations, buffer);
    }

    public void applyState(CheckState state) {
        this.violations = state.violations();
        this.buffer = state.buffer();
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