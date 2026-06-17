package ch.mcserver.goliathPaperCore.module.spawnstash.type;

import ch.mcserver.goliathPaperCore.module.spawnstash.SpawnStash;
import ch.mcserver.goliathPaperCore.module.spawnstash.TemporaryBlocks;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.entity.EntityType;

import java.util.concurrent.ThreadLocalRandom;

public class Line implements SpawnStash {

    @Override
    public String getName() {
        return "line";
    }

    @Override
    public void spawn(Location location) {
        TemporaryBlocks temp = new TemporaryBlocks();

        Block spawnerBlock = location.clone().add(0, 0, 0).getBlock();
        Block spawnerBlock2 = location.clone().add(1, 0, 0).getBlock();
        Block spawnerBlock3 = location.clone().add(2, 0, 0).getBlock();
        Block shulkerBox = location.clone().add(2, 1, 0).getBlock();

        temp.add(spawnerBlock);
        temp.add(spawnerBlock2);
        temp.add(spawnerBlock3);
        temp.add(shulkerBox);

        spawnerBlock.setType(Material.SPAWNER);
        spawnerBlock2.setType(Material.SPAWNER);
        spawnerBlock3.setType(Material.SPAWNER);
        shulkerBox.setType(Material.SHULKER_BOX);


        CreatureSpawner spawner = (CreatureSpawner) spawnerBlock.getState();
        int random = ThreadLocalRandom.current().nextInt(1, 5);
        switch (random) {
            case 1:
                spawner.setSpawnedType(EntityType.SKELETON);
                break;
            case 2:
                spawner.setSpawnedType(EntityType.ZOMBIE);
                break;
            case 3:
                spawner.setSpawnedType(EntityType.CREEPER);
                break;
            case 4:
                spawner.setSpawnedType(EntityType.SPIDER);
                break;
        }
        spawner.setSpawnCount(0);
        spawner.update();

        CreatureSpawner spawner2 = (CreatureSpawner) spawnerBlock2.getState();
        random = ThreadLocalRandom.current().nextInt(1, 5);
        switch (random) {
            case 1:
                spawner2.setSpawnedType(EntityType.SKELETON);
                break;

            case 2:
                spawner2.setSpawnedType(EntityType.ZOMBIE);
                break;

            case 3:
                spawner2.setSpawnedType(EntityType.CREEPER);
                break;

            case 4:
                spawner2.setSpawnedType(EntityType.SPIDER);
                break;
        }
        spawner2.setSpawnCount(0);
        spawner2.update();

        CreatureSpawner spawner3 = (CreatureSpawner) spawnerBlock3.getState();
        random = ThreadLocalRandom.current().nextInt(1, 5);
        switch (random) {
            case 1:
                spawner3.setSpawnedType(EntityType.SKELETON);
                break;

            case 2:
                spawner3.setSpawnedType(EntityType.ZOMBIE);
                break;

            case 3:
                spawner3.setSpawnedType(EntityType.CREEPER);
                break;

            case 4:
                spawner3.setSpawnedType(EntityType.SPIDER);
                break;
        }
        spawner3.setSpawnCount(0);
        spawner3.update();

        temp.startAutoRestore();
    }
}
