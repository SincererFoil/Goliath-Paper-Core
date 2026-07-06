package ch.mcserver.goliathPaperCore.common.database.mongodb;

import ch.mcserver.goliathPaperCore.module.history.HistoryEvent;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Projections;
import com.mongodb.client.model.Sorts;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class HistoryRepository {

    private final MongoCollection<Document> collection;

    public HistoryRepository(MongoCollection<Document> collection) {
        this.collection = collection;
    }

    public void createEvent(UUID uuid, String type, String title, String server, String historyId) {

        if (collection.countDocuments(Filters.eq("uuid", uuid.toString())) > 119) {
            List<ObjectId> ids = collection.find(Filters.eq("uuid", uuid.toString()))
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
                .append("historyId", historyId)
                .append("uuid", uuid.toString())
                .append("type", type)
                .append("title", title)
                .append("server", server)
                .append("createdAt", System.currentTimeMillis());

        collection.insertOne(document);
    }

    public List<HistoryEvent> getEventsByPlayerUUID(UUID uuid) {
        return toList(collection.find(Filters.eq("uuid", uuid.toString()))
                .sort(Sorts.descending("createdAt")));
    }

    public List<HistoryEvent> getAllEvents() {
        return toList(collection.find()
                .sort(Sorts.descending("createdAt")));
    }

    private List<HistoryEvent> toList(FindIterable<Document> iterable) {
        List<HistoryEvent> events = new ArrayList<>();
        for (Document document : iterable) {
            events.add(mapToHistoryEvent(document));
        }
        return events;
    }

    private HistoryEvent mapToHistoryEvent(Document document) {
        return new HistoryEvent(
                document.getString("historyId"),
                UUID.fromString(document.getString("uuid")),
                document.getString("type"),
                document.getString("title"),
                document.getString("server"),
                document.getLong("createdAt")
        );
    }
}