package ch.mcserver.goliathPaperCore.module.anticheat.data;

import com.comphenix.protocol.PacketType;
import java.util.UUID;

public interface PacketData {
    UUID playerUuid();
    PacketType packetType();
}