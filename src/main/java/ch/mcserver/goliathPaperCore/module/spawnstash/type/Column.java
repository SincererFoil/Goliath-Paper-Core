package ch.mcserver.goliathPaperCore.module.spawnstash.type;

import ch.mcserver.goliathPaperCore.module.spawnstash.SpawnStash;
import ch.mcserver.goliathPaperCore.module.spawnstash.TemporaryBlocks;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.entity.EntityType;

import java.util.concurrent.ThreadLocalRandom;

public class Column implements SpawnStash {

    @Override
    public String getName() {
        return "column";
    }

    @Override
    public void spawn(Location location) {
        int random = ThreadLocalRandom.current().nextInt(1, 4);
        switch (random) {
            case 1:
                shulkerTower(location);
                break;
            case 2:
                crafterColumn(location);
                break;
            case 3:
                storageColumn(location);
                break;


        }

    }

    private void shulkerTower(Location location) {
        TemporaryBlocks temp = new TemporaryBlocks();

        Block spawnerBlock = location.clone().add(0, 0, 0).getBlock();
        Block shulkerBox1 = location.clone().add(1, 0, 0).getBlock();
        Block shulkerBox2 = location.clone().add(1, 1, 0).getBlock();
        Block barrel = location.clone().add(1, 2, 0).getBlock();
        Block enderchest = location.clone().add(0, 0, 1).getBlock();
        Block cake = location.clone().add(0, 1, 1).getBlock();

        temp.add(spawnerBlock);
        temp.add(shulkerBox1);
        temp.add(shulkerBox2);
        temp.add(barrel);
        temp.add(enderchest);
        temp.add(cake);

        spawnerBlock.setType(Material.SPAWNER);
        shulkerBox1.setType(Material.SHULKER_BOX);
        shulkerBox2.setType(Material.SHULKER_BOX);
        barrel.setType(Material.BARREL);
        enderchest.setType(Material.ENDER_CHEST);
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


    private void crafterColumn(Location location) {
        TemporaryBlocks temp = new TemporaryBlocks();

        Block spawnerBlock = location.clone().add(0, 0, 0).getBlock();
        Block amethystBlock = location.clone().add(1, 0, 0).getBlock();
        Block craftingTableBlock = location.clone().add(1, 1, 0).getBlock();
        Block amethyst = location.clone().add(1, 2, 0).getBlock();

        temp.add(spawnerBlock);
        temp.add(amethystBlock);
        temp.add(craftingTableBlock);
        temp.add(amethyst);


        spawnerBlock.setType(Material.SPAWNER);
        amethystBlock.setType(Material.AMETHYST_BLOCK);
        craftingTableBlock.setType(Material.CRAFTING_TABLE);
        amethyst.setType(Material.AMETHYST_CLUSTER);


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

    private void storageColumn(Location location) {
        TemporaryBlocks temp = new TemporaryBlocks();

        Block spawnerBlock = location.clone().add(0, 0, 0).getBlock();
        Block amethystBlock = location.clone().add(1, 0, 0).getBlock();
        Block amethystBlock2 = location.clone().add(1, 2, 0).getBlock();
        Block chest = location.clone().add(1, 3, 0).getBlock();

        temp.add(spawnerBlock);
        temp.add(amethystBlock);
        temp.add(amethystBlock2);
        temp.add(chest);

        spawnerBlock.setType(Material.SPAWNER);
        amethystBlock.setType(Material.AMETHYST_BLOCK);
        amethystBlock2.setType(Material.AMETHYST_BLOCK);
        chest.setType(Material.CHEST);

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
