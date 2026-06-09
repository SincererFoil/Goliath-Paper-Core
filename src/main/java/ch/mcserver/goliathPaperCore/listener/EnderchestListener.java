package ch.mcserver.goliathPaperCore.listener;

import ch.mcserver.goliathPaperCore.GoliathPaperCore;
import ch.mcserver.goliathPaperCore.service.EnderchestService;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.EnderChest;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerInteractEvent;

public class EnderchestListener implements Listener {
    private final EnderchestService enderchestService;


    public EnderchestListener(EnderchestService enderchestService) {
        this.enderchestService = enderchestService;
    }


    @EventHandler
    public void onEnderChestOpen(PlayerInteractEvent event) {
        Block block = event.getClickedBlock();

        if(event.getClickedBlock() == null ) {
            return;
        }

        if (event.getAction() != Action.RIGHT_CLICK_BLOCK) {
            return;
        }

        if (event.getClickedBlock().getType() != Material.ENDER_CHEST) {
            return;
        }

        event.setCancelled(true);

        if(block.getState() instanceof EnderChest enderchest) {
            enderchest.open();
        }
        Player player = event.getPlayer();
        player.playSound(block.getLocation(), Sound.BLOCK_ENDER_CHEST_OPEN, 1.0f, 1.0f);

        enderchestService.openEnderchest(player);
    }

    @EventHandler
    public void onClose(InventoryCloseEvent event) {

        if (!(event.getPlayer() instanceof Player player)) {
            return;
        }

        if (!event.getView().getTitle().equals("Ender Chest")) {
            return;
        }

        Block block = player.getTargetBlockExact(6);

        if (block != null && block.getType() == Material.ENDER_CHEST) {
            if (block.getState() instanceof EnderChest enderchest) {
                enderchest.close();
            }
            player.playSound(block.getLocation(), Sound.BLOCK_ENDER_CHEST_CLOSE, 1.0f, 1.0f);
        }

    }


}
