package ch.mcserver.goliathPaperCore.module.anticheat.inspection.player;

import java.util.List;
import java.util.UUID;

public record SuspectEntry(

        UUID playerUuid,
        String playerName,
        List<SuspectCheckEntry> checks

) {
}
