package ch.mcserver.goliathPaperCore.module.randomteleport;

import java.util.UUID;

public record PreparedLocation(
        UUID requestUuid,
        UUID playerUuid,
        String targetServer,
        String world,
        double x,
        double y,
        double z,
        float yaw,
        float pitch
) {
}