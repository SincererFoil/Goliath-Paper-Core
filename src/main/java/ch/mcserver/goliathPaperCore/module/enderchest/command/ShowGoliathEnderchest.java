package ch.mcserver.goliathPaperCore.module.enderchest.command;

import ch.mcserver.goliathPaperCore.module.enderchest.PlayerEnderchestRepository;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;

public class ShowGoliathEnderchest {

    private final PlayerEnderchestRepository enderchestRepository;

    public ShowGoliathEnderchest(PlayerEnderchestRepository enderchestRepository) {
        this.enderchestRepository = enderchestRepository;
    }

    public void showEnderchest(Player player, UUID uuid, String targetName, boolean editable) {
        Inventory inventory = Bukkit.createInventory(
                new AdminEnderchestHolder(uuid, editable),
                54,
                targetName + "'s Enderchest"
        );
        ItemStack[] itemsInEnderchest = enderchestRepository.loadEnderchest(uuid);
        inventory.setContents(itemsInEnderchest);
        player.openInventory(inventory);
    }

    public static class AdminEnderchestHolder implements InventoryHolder {

        private final UUID owner;
        private final boolean editable;

        public AdminEnderchestHolder(UUID owner, boolean editable) {
            this.owner = owner;
            this.editable = editable;
        }

        public UUID getOwner() {
            return owner;
        }

        public boolean isEditable() {
            return editable;
        }

        @Override
        public Inventory getInventory() {
            return null;
        }
    }
}
