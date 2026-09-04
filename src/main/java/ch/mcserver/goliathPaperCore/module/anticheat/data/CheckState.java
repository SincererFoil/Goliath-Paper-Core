package ch.mcserver.goliathPaperCore.module.anticheat.data;

import java.util.UUID;

public record CheckState(
        UUID playerUuid,
        String checkId,
        int violations,
        double buffer
) {
}
