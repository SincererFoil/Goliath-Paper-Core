package ch.mcserver.goliathPaperCore.database.mongodb.repository;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.UpdateOptions;
import org.bson.Document;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.Base64;
import java.util.UUID;

public class PlayerEnderchestRepository {

    private final MongoCollection<Document> collection;

    public PlayerEnderchestRepository(MongoCollection<Document> collection) {
        this.collection = collection;
    }

    public void createEnderchest(UUID playerUUID) {
        if (playerEnderchestExists(playerUUID)) {
            return;
        }

        Document playerEnderChest = new Document()
                .append("uuid", playerUUID.toString())
                .append("created_at", System.currentTimeMillis())
                .append("enderchest", toBase64(new ItemStack[54]));

        collection.insertOne(playerEnderChest);
    }

    public void saveEnderchest(UUID uuid, ItemStack[] contents) {
        String base64 = toBase64(contents);

        collection.updateOne(
                Filters.eq("uuid", uuid.toString()),
                new Document("$set", new Document("enderchest", base64)),
                new UpdateOptions().upsert(true)
        );
    }

    public ItemStack[] loadEnderchest(UUID uuid) {
        Document document = collection.find(Filters.eq("uuid", uuid.toString())).first();

        if (document == null || !document.containsKey("enderchest")) {
            return new ItemStack[54];
        }

        return fromBase64(document.getString("enderchest"));
    }

    public boolean playerEnderchestExists(UUID playerUUID) {
        return collection.find(Filters.eq("uuid", playerUUID.toString())).first() != null;
    }

    public static String toBase64(ItemStack[] items) {
        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            BukkitObjectOutputStream dataOutput = new BukkitObjectOutputStream(outputStream);

            dataOutput.writeInt(items.length);

            for (ItemStack item : items) {
                dataOutput.writeObject(item);
            }

            dataOutput.close();

            return Base64.getEncoder().encodeToString(outputStream.toByteArray());
        } catch (Exception exception) {
            throw new RuntimeException(exception);
        }
    }

    public static ItemStack[] fromBase64(String data) {
        try {
            ByteArrayInputStream inputStream =
                    new ByteArrayInputStream(Base64.getDecoder().decode(data));

            BukkitObjectInputStream dataInput =
                    new BukkitObjectInputStream(inputStream);

            int length = dataInput.readInt();
            ItemStack[] items = new ItemStack[length];

            for (int i = 0; i < length; i++) {
                items[i] = (ItemStack) dataInput.readObject();
            }

            dataInput.close();

            return items;
        } catch (Exception exception) {
            throw new RuntimeException(exception);
        }
    }
}