package ch.mcserver.goliathPaperCore.module.anticheat.checks;

import ch.mcserver.goliathPaperCore.module.anticheat.data.PlayerData;

public class PitchA {

    private final PlayerData playerData;

    private double buffer;

    public PitchA(PlayerData playerData) {
        this.playerData = playerData;
    }

    public void handle() {
        float pitch = playerData.getPitch();

        if (pitch > 90 || pitch < -90) {
            buffer += 0.2;
        } else {
            buffer = Math.max(0.0, buffer - 0.1);
        }

        if (buffer >= 1.0) {
            // flag
        }


    }
}