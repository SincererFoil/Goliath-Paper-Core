package ch.mcserver.goliathPaperCore.module.anticheat.flag;

import java.util.UUID;

public record FlagData(
        UUID playerUuid,
        String playerName,
        FlagType checkName,
        int violations,
        String details,
        String serverName,
        long timestamp
) {
}
