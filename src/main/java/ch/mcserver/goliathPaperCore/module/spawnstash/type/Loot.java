package ch.mcserver.goliathPaperCore.module.spawnstash.type;

import ch.mcserver.goliathPaperCore.module.spawnstash.SpawnStash;
import ch.mcserver.goliathPaperCore.module.spawnstash.TemporaryBlocks;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.entity.EntityType;

import java.util.concurrent.ThreadLocalRandom;

public class Loot implements SpawnStash {

    @Override
    public String getName() {
        return "loot";
    }

    @Override
    public void spawn(Location location) {
        int random = ThreadLocalRandom.current().nextInt(1, 4);
        switch (random) {
            case 1:
                cakeLootStash(location);
                break;
            case 2:
                amethystLootStash(location);
                break;
            case 3:
                shulkerBoxLootStash(location);
                break;

        }
    }

    private void cakeLootStash(Location location) {
        TemporaryBlocks temp = new TemporaryBlocks();

        Block spawnerBlock = location.clone().add(0, 0, 0).getBlock();
        Block tuffBricks = location.clone().add(1, 0, 0).getBlock();
        Block cake = location.clone().add(1, 1, 0).getBlock();
        Block enderchest = location.clone().add(0, 0, 1).getBlock();

        temp.add(spawnerBlock);
        temp.add(tuffBricks);
        temp.add(enderchest);
        temp.add(enderchest);
        temp.add(cake);

        spawnerBlock.setType(Material.SPAWNER);
        tuffBricks.setType(Material.TUFF_BRICKS);
        cake.setType(Material.CAKE);
        enderchest.setType(Material.ENDER_CHEST);

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
        temp.startAutoRestore();
    }

    private void amethystLootStash(Location location) {
        TemporaryBlocks temp = new TemporaryBlocks();

        Block spawnerBlock = location.clone().add(0, 0, 0).getBlock();
        Block enderchest = location.clone().add(0, 0, 1).getBlock();
        Block smoothBasalt = location.clone().add(1, 0, 0).getBlock();
        Block amethystCluster = location.clone().add(1, 1, 0).getBlock();

        temp.add(spawnerBlock);
        temp.add(enderchest);
        temp.add(smoothBasalt);
        temp.add(amethystCluster);

        spawnerBlock.setType(Material.SPAWNER);
        enderchest.setType(Material.ENDER_CHEST);
        smoothBasalt.setType(Material.SMOOTH_BASALT);
        amethystCluster.setType(Material.AMETHYST_CLUSTER);

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
        temp.startAutoRestore();
    }

    private void shulkerBoxLootStash(Location location) {
        TemporaryBlocks temp = new TemporaryBlocks();

        Block spawnerBlock = location.clone().add(0, 0, 0).getBlock();
        Block shulkerBox = location.clone().add(0, 0, 1).getBlock();

        temp.add(spawnerBlock);
        temp.add(shulkerBox);

        spawnerBlock.setType(Material.SPAWNER);
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
        temp.startAutoRestore();
    }
}
