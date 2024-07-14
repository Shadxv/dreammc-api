package org.sproject.sprojectapi.api.database;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

public class MongoService {

    private static final MongoConnectionManager mongoConnectionManager = new MongoConnectionManager();
    private static final Map<String, MongoDatabase> cachedDatabases = new HashMap<>();
    private static final Map<String, MongoCollection<Document>> cachedDocumentCollections = new HashMap<>();
    private static final Map<String, MongoCollection<?>> cachedMappedCollections = new HashMap<>();

    public static boolean init() {
        if(!System.getenv().containsKey("MONGODB_URI")) return false;
        mongoConnectionManager.setupConnection(System.getenv("MONGODB_URI"));
        return true;
    }

    @Nullable
    public static MongoDatabase getDatabase(String name) {
        if(cachedDatabases.containsKey(name)) return cachedDatabases.get(name);

        MongoDatabase database = mongoConnectionManager.getDatabase(name);
        if(database != null) cachedDatabases.put(name, database);
        return database;
    }

    @Nullable
    public static MongoCollection<Document> getCollection(@NotNull MongoDatabase database, String name) {
        if(cachedDocumentCollections.containsKey(name)) return cachedDocumentCollections.get(name);

        MongoCollection<Document> collection = mongoConnectionManager.getCollection(database, name);
        if(collection != null) cachedDocumentCollections.put(name, collection);
        return collection;
    }

    @Nullable
    public static <T> MongoCollection<T> getCollection(@NotNull MongoDatabase database, String name, Class<T> mapperClass) {
        if(cachedMappedCollections.containsKey(name)) {
            try {
                //noinspection unchecked
                return (MongoCollection<T>) cachedMappedCollections.get(name);
            } catch (ClassCastException e) {
                return null;
            }
        }

        MongoCollection<T> collection = mongoConnectionManager.getCollection(database, name, mapperClass);
        if(collection != null) cachedMappedCollections.put(name, collection);
        return collection;
    }


}
