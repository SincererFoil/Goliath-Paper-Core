package ch.mcserver.goliathPaperCore.common.database.mongodb;

import ch.mcserver.goliathPaperCore.module.chat.ChatMessage;
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

public class ChatLogRepository {

    private final MongoCollection<Document> collection;

    public ChatLogRepository(MongoCollection<Document> collection) {
        this.collection = collection;
    }

    public void createEvent(UUID uuid, String server, String message, String username) {
        Document document = new Document()
                .append("uuid", uuid.toString())
                .append("server", server)
                .append("username", username)
                .append("message", message)
                .append("createdAt", System.currentTimeMillis());

        collection.insertOne(document);

        long count = collection.countDocuments(Filters.eq("uuid", uuid.toString()));
        if (count > 120) {
            List<ObjectId> ids = collection.find(Filters.eq("uuid", uuid.toString()))
                    .sort(Sorts.ascending("createdAt"))
                    .limit((int) (count - 120))
                    .projection(Projections.include("_id"))
                    .into(new ArrayList<>())
                    .stream()
                    .map(d -> d.getObjectId("_id"))
                    .toList();

            if (!ids.isEmpty()) {
                collection.deleteMany(Filters.in("_id", ids));
            }
        }
    }

    public List<ChatMessage> getMessagesByPlayerUUID(UUID uuid) {
        return toList(collection.find(Filters.eq("uuid", uuid.toString()))
                .sort(Sorts.descending("createdAt")));
    }


    public List<ChatMessage> getAllMessages() {
        return toList(collection.find()
                .sort(Sorts.descending("createdAt")));
    }

    private List<ChatMessage> toList(FindIterable<Document> iterable) {
        List<ChatMessage> events = new ArrayList<>();
        for (Document document : iterable) {
            events.add(mapToChatEvent(document));
        }
        return events;
    }

    private ChatMessage mapToChatEvent(Document document) {
        return new ChatMessage(
                UUID.fromString(document.getString("uuid")),
                document.getString("server"),
                document.getString("username"),
                document.getString("message"),
                document.getLong("createdAt")
        );
    }
}
