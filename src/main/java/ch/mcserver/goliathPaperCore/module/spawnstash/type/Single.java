package ch.mcserver.goliathPaperCore.module.spawnstash.type;

import ch.mcserver.goliathPaperCore.module.spawnstash.SpawnStash;
import ch.mcserver.goliathPaperCore.module.spawnstash.TemporaryBlocks;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.entity.EntityType;

import java.util.concurrent.ThreadLocalRandom;

public class Single implements SpawnStash {

    @Override
    public String getName() {
        return "single";
    }

    @Override
    public void spawn(Location location) {
        int random = ThreadLocalRandom.current().nextInt(1, 7);
        switch (random) {
            case 1:
                crafterSingleStash(location);
                break;
            case 2:
                snowSingleStash(location);
                break;
            case 3:
                oneSingleStash(location);
                break;
            case 4:
                smallCraftingSingleStash(location);
                break;
            case 5:
                bigEnderchestCraftingSingleStash(location);
                break;
            case 6:
                candleSingleStash(location);
                break;
        }
    }

    private void crafterSingleStash(Location location) {
        TemporaryBlocks temp = new TemporaryBlocks();

        Block spawnerBlock = location.clone().add(0, 0, 0).getBlock();
        Block craftingTable1 = location.clone().add(1, 0, 0).getBlock();
        Block craftingTable2 = location.clone().add(0, 0, -1).getBlock();
        Block decoration1 = location.clone().add(1, 1, 0).getBlock();
        Block decoration2 = location.clone().add(0, 1, -1).getBlock();

        temp.add(spawnerBlock);
        temp.add(craftingTable1);
        temp.add(craftingTable2);
        temp.add(decoration1);
        temp.add(decoration2);

        spawnerBlock.setType(Material.SPAWNER);
        craftingTable1.setType(Material.CRAFTING_TABLE);
        craftingTable2.setType(Material.CRAFTING_TABLE);
        decoration1.setType(Material.FLOWER_POT);
        decoration2.setType(Material.WHITE_CANDLE);

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

    private void snowSingleStash(Location location) {
        TemporaryBlocks temp = new TemporaryBlocks();

        Block spawnerBlock = location.clone().add(0, 0, 0).getBlock();
        Block snowBlock = location.clone().add(0, 0, -1).getBlock();
        Block cake = location.clone().add(0, 1, -1).getBlock();

        temp.add(spawnerBlock);
        temp.add(snowBlock);
        temp.add(cake);

        spawnerBlock.setType(Material.SPAWNER);
        snowBlock.setType(Material.SNOW_BLOCK);
        cake.setType(Material.CAKE);

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

    private void oneSingleStash(Location location) {
        TemporaryBlocks temp = new TemporaryBlocks();

        Block spawnerBlock = location.clone().add(0, 0, 0).getBlock();

        temp.add(spawnerBlock);

        spawnerBlock.setType(Material.SPAWNER);

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

    private void smallCraftingSingleStash(Location location) {
        TemporaryBlocks temp = new TemporaryBlocks();
        Block spawnerBlock = location.clone().add(0, 0, 0).getBlock();
        Block craftingTable = location.clone().add(0, 0, -1).getBlock();
        Block amethystCluster = location.clone().add(0, 1, -1).getBlock();

        temp.add(spawnerBlock);
        temp.add(craftingTable);
        temp.add(amethystCluster);

        spawnerBlock.setType(Material.SPAWNER);
        craftingTable.setType(Material.CRAFTING_TABLE);
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

    private void bigEnderchestCraftingSingleStash(Location location) {
        TemporaryBlocks temp = new TemporaryBlocks();
        Block spawnerBlock = location.clone().add(0, 0, 0).getBlock();
        Block craftingTable = location.clone().add(1, 0, 0).getBlock();
        Block enderchest = location.clone().add(0, 0, -1).getBlock();
        Block amethystCluster = location.clone().add(1, 1, 0).getBlock();
        Block amethystCluster2 = location.clone().add(0, 1, -1).getBlock();

        temp.add(spawnerBlock);
        temp.add(craftingTable);
        temp.add(enderchest);
        temp.add(amethystCluster);
        temp.add(amethystCluster2);

        spawnerBlock.setType(Material.SPAWNER);
        craftingTable.setType(Material.CRAFTING_TABLE);
        enderchest.setType(Material.ENDER_CHEST);
        amethystCluster.setType(Material.AMETHYST_CLUSTER);
        amethystCluster2.setType(Material.AMETHYST_CLUSTER);

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

    private void candleSingleStash(Location location) {
        TemporaryBlocks temp = new TemporaryBlocks();
        Block spawnerBlock = location.clone().add(0, 0, 0).getBlock();
        Block enderchest = location.clone().add(0, 0, -1).getBlock();
        Block candle = location.clone().add(0, 1, -1).getBlock();

        temp.add(spawnerBlock);
        temp.add(enderchest);
        temp.add(candle);

        spawnerBlock.setType(Material.SPAWNER);
        enderchest.setType(Material.ENDER_CHEST);
        candle.setType(Material.CANDLE);

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
