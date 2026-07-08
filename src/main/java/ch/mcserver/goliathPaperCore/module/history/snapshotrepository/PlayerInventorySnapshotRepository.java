package ch.mcserver.goliathPaperCore.module.history.snapshotrepository;

import com.mongodb.client.MongoCollection;
import org.bson.Document;

public class PlayerInventorySnapshotRepository {
    private final MongoCollection<Document> collection;

    public PlayerInventorySnapshotRepository(MongoCollection<Document> collection) {
        this.collection = collection;
    }


}
