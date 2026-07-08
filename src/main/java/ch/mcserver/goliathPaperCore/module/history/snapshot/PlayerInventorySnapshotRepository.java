package ch.mcserver.goliathPaperCore.module.history.snapshot;

import ch.mcserver.goliathPaperCore.GoliathPaperCore;
import com.mongodb.MongoException;
import com.mongodb.MongoWriteException;
import com.mongodb.ErrorCategory;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Projections;
import com.mongodb.client.model.Sorts;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;

public class PlayerInventorySnapshotRepository {
    private final MongoCollection<Document> collection;

    public PlayerInventorySnapshotRepository(MongoCollection<Document> collection) {
        this.collection = collection;
    }

    public boolean createInventorySnapshot(UUID playerUUID, UUID historyUUID, String inventory, String armor, String offhand) {

        if (collection.countDocuments(Filters.eq("playerUUID", playerUUID.toString())) > 119) {
            List<ObjectId> ids = collection.find(Filters.eq("playerUUID", playerUUID.toString()))
                    .sort(Sorts.ascending("createdAt"))
                    .limit(1)
                    .projection(Projections.include("_id"))
                    .into(new ArrayList<>())
                    .stream()
                    .map(d -> d.getObjectId("_id"))
                    .toList();

            collection.deleteMany(Filters.in("_id", ids));
        }

        Document document = new Document()
                .append("playerUUID", playerUUID.toString())
                .append("historyUUID", historyUUID.toString())
                .append("inventory", inventory)
                .append("armor", armor)
                .append("offhand", offhand)
                .append("createdAt", System.currentTimeMillis());

        try {
            collection.insertOne(document);
            return true;
        } catch (MongoWriteException exception) {
            if (exception.getError().getCategory() == ErrorCategory.DUPLICATE_KEY) {
                return false;
            }
            GoliathPaperCore.getInstance().logger.log(Level.WARNING,
                    "Failed to create inventory snapshot for player " + playerUUID, exception);
            return false;
        }
    }

    public Document getInventoryByHistoryUUID(UUID historyUUID) {
        try {
            return collection.find(Filters.eq("historyUUID", historyUUID.toString())).first();
        } catch (MongoException exception) {
            GoliathPaperCore.getInstance().logger.log(Level.WARNING,
                    "Failed to load inventory snapshot for history " + historyUUID, exception);
            return null;
        }
    }
}
