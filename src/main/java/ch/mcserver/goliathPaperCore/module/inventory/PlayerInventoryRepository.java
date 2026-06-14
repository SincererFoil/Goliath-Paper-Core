package ch.mcserver.goliathPaperCore.module.inventory;

import ch.mcserver.goliathPaperCore.GoliathPaperCore;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.UpdateOptions;
import org.bson.Document;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;
import java.util.logging.Level;

import static ch.mcserver.goliathPaperCore.module.enderchest.PlayerEnderchestRepository.fromBase64;
import static ch.mcserver.goliathPaperCore.module.enderchest.PlayerEnderchestRepository.toBase64;

public class PlayerInventoryRepository {

    private final MongoCollection<Document> collection;

    public PlayerInventoryRepository(MongoCollection<Document> collection) {
        this.collection = collection;
    }


    public void saveInventory(UUID playerUUID) {
        Player player = Bukkit.getPlayer(playerUUID);
        if (player == null) return;

        Document update = new Document()
                .append("updated_at", System.currentTimeMillis())
                .append("armor", toBase64(player.getInventory().getArmorContents()))
                .append("inventory", toBase64(player.getInventory().getContents()))
                .append("offhand", toBase64(new ItemStack[]{
                        player.getInventory().getItemInOffHand()
                }));

        collection.updateOne(
                Filters.eq("uuid", playerUUID.toString()),
                new Document("$set", update),
                new UpdateOptions().upsert(true)
        );

    }

    public void loadInventory(UUID playerUUID) {
        Player player = Bukkit.getPlayer(playerUUID);
        if (player == null) return;
        Document document = collection.find(Filters.eq("uuid", playerUUID.toString())).first();
        if (document == null) {
            saveInventory(playerUUID);
            document = collection.find(Filters.eq("uuid", playerUUID.toString())).first();
        }
        try {
            player.getInventory().setContents(fromBase64(document.getString("inventory")));
            player.getInventory().setArmorContents(fromBase64(document.getString("armor")));
            ItemStack[] offhand = fromBase64(document.getString("offhand"));
            if (offhand.length > 0) {
                player.getInventory().setItemInOffHand(offhand[0]);
            }
        } catch (Exception e) {
            GoliathPaperCore.getInstance().logger.log(Level.WARNING, "Could not load Inventory from " + player.getName() + ". Error message: ", e);
        }

    }

    public boolean inventoryExist(UUID playerUUID) {
        return collection.find(Filters.eq("uuid", playerUUID.toString())).first() != null;
    }


}

