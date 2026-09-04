package ch.mcserver.goliathPaperCore.module.anticheat.checks;

import ch.mcserver.goliathPaperCore.module.anticheat.data.PlayerData;

public abstract class Check {

    protected final PlayerData playerData;
    protected double violations;

    protected Check(PlayerData playerData) {
        this.playerData = playerData;
    }

    protected void flag(String details) {
        violations++;

//        FlagManager.flag(
//                playerData,
//                getClass().getSimpleName(),
//                violations,
//                details
//        );
    }
}