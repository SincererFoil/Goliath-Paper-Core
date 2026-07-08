package ch.mcserver.goliathPaperCore.module.spawnstash.type;

import ch.mcserver.goliathPaperCore.module.spawnstash.SpawnStash;
import ch.mcserver.goliathPaperCore.module.spawnstash.TemporaryBlocks;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.entity.EntityType;

import java.util.concurrent.ThreadLocalRandom;

public class Pedestal implements SpawnStash {

    @Override
    public String getName() {
        return "pedestal";
    }

    @Override
    public void spawn(Location location) {
        TemporaryBlocks temp = new TemporaryBlocks();

        Block spawnerBlock = location.clone().add(0, 0, 0).getBlock();
        Block amethystBlock = location.clone().add(1, 0, 0).getBlock();
        Block decoration1 = location.clone().add(1, 1, 0).getBlock();
        Block enderchest = location.clone().add(1, 0, 1).getBlock();
        Block craftingTable = location.clone().add(2, 0, 1).getBlock();
        Block craftingTable2 = location.clone().add(0, 0, -1).getBlock();
        Block cake = location.clone().add(0, 1, -1).getBlock();

        temp.add(spawnerBlock);
        temp.add(amethystBlock);
        temp.add(decoration1);
        temp.add(enderchest);
        temp.add(craftingTable);
        temp.add(craftingTable2);
        temp.add(cake);

        spawnerBlock.setType(Material.SPAWNER);
        amethystBlock.setType(Material.AMETHYST_BLOCK);
        decoration1.setType(Material.AMETHYST_CLUSTER);
        enderchest.setType(Material.ENDER_CHEST);
        craftingTable.setType(Material.CRAFTING_TABLE);
        craftingTable2.setType(Material.CRAFTING_TABLE);
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
}
