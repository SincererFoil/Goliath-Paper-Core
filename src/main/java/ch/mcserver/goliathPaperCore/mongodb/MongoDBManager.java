package ch.mcserver.goliathPaperCore.mongodb;

import ch.mcserver.goliathPaperCore.GoliathPaperCore;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

import java.util.logging.Level;
import java.util.logging.Logger;

public class MongoDBManager {

    private MongoClient mongoClient;
    private MongoDatabase mongoDatabase;
    private Logger logger = GoliathPaperCore.getInstance().getLogger();
    public void connect() {
        try {
            String uri = GoliathPaperCore.config.node("mongodb", "uri").toString();

            mongoClient = MongoClients.create(uri);
            mongoDatabase = mongoClient.getDatabase("goliath");
            logger.log(Level.INFO, "[Goliath] MongoDB connected");
        } catch (Exception e) {
            logger.log(Level.SEVERE, "[Goliath] MongoDB connection failed", e);
        }
    }

    public MongoDatabase getMongoDatabase() {
        if (mongoDatabase == null) {
            logger.log(Level.SEVERE, "[Goliath] MongoDB is not Connected");
        }
        return mongoDatabase;
    }

    public MongoCollection<Document> getMongoCollection(String name) {return mongoDatabase.getCollection(name);}

    public void disconnect() {
        if (mongoClient != null) {
            mongoClient.close();
            logger.log(Level.INFO, "[Goliath] MongoDB closed");
        }}

}
