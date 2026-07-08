package ch.mcserver.goliathPaperCore.module.history.gui;

import java.util.UUID;

public record HistoryEvent(
        String historyId,
        UUID uuid,
        String type,
        String title,
        String server,
        long createdAt
)
{

}
