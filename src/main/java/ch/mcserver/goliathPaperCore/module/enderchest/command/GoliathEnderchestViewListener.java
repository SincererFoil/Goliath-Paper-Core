package ch.mcserver.goliathPaperCore.module.enderchest.command;

import ch.mcserver.goliathPaperCore.module.enderchest.PlayerEnderchestRepository;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;

public class GoliathEnderchestViewListener implements Listener {

    private final PlayerEnderchestRepository enderchestRepository;

    public GoliathEnderchestViewListener(PlayerEnderchestRepository enderchestRepository) {
        this.enderchestRepository = enderchestRepository;
    }

    @EventHandler
    public void onClick(InventoryClickEvent event) {
        if (!(event.getInventory().getHolder() instanceof ShowGoliathEnderchest.AdminEnderchestHolder holder)) {
            return;
        }
        if (!holder.isEditable()) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onClose(InventoryCloseEvent event) {
        if (!(event.getInventory().getHolder() instanceof ShowGoliathEnderchest.AdminEnderchestHolder holder)) {
            return;
        }
        if (!holder.isEditable()) {
            return;
        }
        enderchestRepository.saveEnderchest(
                holder.getOwner(),
                event.getInventory().getContents()
        );
    }
}
