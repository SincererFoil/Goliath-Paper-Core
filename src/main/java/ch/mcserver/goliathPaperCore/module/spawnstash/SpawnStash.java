package ch.mcserver.goliathPaperCore.module.spawnstash;

import org.bukkit.Location;

public interface SpawnStash {

    String getName();

    void spawn(Location location);
}