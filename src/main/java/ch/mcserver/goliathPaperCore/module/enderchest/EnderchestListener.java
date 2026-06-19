package ch.mcserver.goliathPaperCore.module.enderchest;

import org.bukkit.Bukkit;
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
import org.bukkit.inventory.ItemStack;

import static ch.mcserver.goliathPaperCore.module.enderchest.EnderchestHolder.openedEnderChests;

public class EnderchestListener implements Listener {
    private final EnderchestService enderchestService;
    private PlayerEnderchestRepository enderchestRepository;


    public EnderchestListener(EnderchestService enderchestService, PlayerEnderchestRepository enderchestRepository) {
        this.enderchestService = enderchestService;
        this.enderchestRepository = enderchestRepository;
    }


    @EventHandler
    public void onEnderChestOpen(PlayerInteractEvent event) {
        Block block = event.getClickedBlock();
        Player player = event.getPlayer();
        if (block == null) {
            return;
        }
        if (event.getAction() != Action.RIGHT_CLICK_BLOCK) {
            return;
        }
        if (block.getType() != Material.ENDER_CHEST) {
            return;
        }

        if (player.isSneaking()) {
            ItemStack itemInHand = event.getItem();
            if (itemInHand != null && itemInHand.getType().isBlock()) {
                return;
            }
            event.setCancelled(true);
            return;
        }
        if (block.getState() instanceof EnderChest enderChest) {
            openedEnderChests.put(player.getUniqueId(), enderChest);
            enderChest.open();
        }

        event.setCancelled(true);
        player.playSound(block.getLocation(), Sound.BLOCK_ENDER_CHEST_OPEN, 0.5f, 1.0f);
        enderchestService.openEnderchest(player);
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {

        if (!(event.getInventory().getHolder() instanceof EnderchestHolder holder)) {
            return;
        }
        enderchestRepository.saveEnderchest(
                holder.getOwner(),
                event.getInventory().getContents()
        );
        enderchestService.removeOpenInventory(holder.getOwner());
        if (openedEnderChests.containsKey(event.getPlayer().getUniqueId())) {
            EnderChest enderchest = openedEnderChests.get(event.getPlayer().getUniqueId());
            enderchest.close();
            Player player = Bukkit.getServer().getPlayer(event.getPlayer().getUniqueId());
            assert player != null;
            player.playSound(player.getLocation(), Sound.BLOCK_ENDER_CHEST_CLOSE, 0.5f, 1.0f);
            openedEnderChests.remove(event.getPlayer().getUniqueId());
        }
    }

}
