package ch.mcserver.goliathPaperCore.module.chat;

import java.util.UUID;

public record ChatMessage(UUID uuid, String name, String server, String message, long Timestamp) {
}
