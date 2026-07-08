package ch.mcserver.goliathPaperCore.module.spawnstash;

import ch.mcserver.goliathPaperCore.GoliathPaperCore;
import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;

import java.util.HashMap;
import java.util.Map;

public class TemporaryBlocks {

    private final Map<Block, BlockData> blocks = new HashMap<>();

    public void add(Block block) {
        blocks.put(block, block.getBlockData().clone());
    }

    public void startAutoRestore() {

        Bukkit.getScheduler().runTaskLater(
                GoliathPaperCore.getInstance(),
                this::restore,
                20L * 60L * 2L
        );
    }

    private void restore() {

        for (Map.Entry<Block, BlockData> entry : blocks.entrySet()) {
            entry.getKey().setBlockData(entry.getValue());
        }

        blocks.clear();
    }
}
