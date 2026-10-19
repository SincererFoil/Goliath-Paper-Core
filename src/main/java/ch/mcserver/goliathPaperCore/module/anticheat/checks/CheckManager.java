package ch.mcserver.goliathPaperCore.module.anticheat.checks;

import ch.mcserver.goliathPaperCore.module.anticheat.data.CheckState;
import ch.mcserver.goliathPaperCore.module.anticheat.data.player.PacketData.PacketData;
import ch.mcserver.goliathPaperCore.module.anticheat.data.player.playerdata.PlayerData;
import ch.mcserver.goliathPaperCore.module.anticheat.flag.FlagManager;

import java.util.ArrayList;
import java.util.List;

public class CheckManager {

    private final List<Check> checks;

    public CheckManager(PlayerData playerData, FlagManager flagManager) {
        this.checks = new ArrayList<>();
        // Initializes Invalid A check
        checks.add(new InvalidA(playerData, flagManager));
    }

    public void handle(PacketData data) {
        for (Check check : checks) {
            if (check.getPacketTypes().contains(data.packetType())) {
                check.decayViolations();
                check.handle(data);
            }
        }
    }

    public List<CheckState> createStates() {

        List<CheckState> states = new ArrayList<>();

        for (Check check : checks) {
            states.add(check.createState());
        }
        return states;
    }

    public void applyStates(List<CheckState> states) {
        states.forEach(state -> {
            checks.stream().filter(check -> check.getFlagType().name().equals(state.checkId()))
                    .findFirst().ifPresent(check -> {
                        check.applyState(state);
                    });
        });
    }

}
