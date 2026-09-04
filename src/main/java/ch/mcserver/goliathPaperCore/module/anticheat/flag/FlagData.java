package ch.mcserver.goliathPaperCore.module.anticheat.flag;

import java.util.UUID;

public record FlagData(
        UUID playerUuid,
        String playerName,
        String checkName,
        double violations,
        String details,
        String serverName,
        long timestamp
) {
}
