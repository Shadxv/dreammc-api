package pl.dreammc.dreammcapi.api.database;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import org.bson.Document;
import org.bson.codecs.Codec;
import org.bson.codecs.DecoderContext;
import org.bson.conversions.Bson;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pl.dreammc.dreammcapi.api.logger.Logger;

import java.util.*;

public class MongoService {

    private static final MongoConnectionManager mongoConnectionManager = new MongoConnectionManager();
    private static final Map<String, MongoDatabase> cachedDatabases = new HashMap<>();
    private static final Map<String, MongoCollection<Document>> cachedDocumentCollections = new HashMap<>();
    private static final Map<String, MongoCollection<?>> cachedMappedCollections = new HashMap<>();

    public static void registerCodec(Codec<?> codec) {
        mongoConnectionManager.addCodec(codec);
    }

    @Nullable
    public static <T> Codec<T> getCodec(Class<T> clazz) {
        return mongoConnectionManager.getCodec(clazz);
    }

    @Nullable
    public static <T> T decode(Document document, String key, Class<T> clazz) {
        Codec<T> codec = getCodec(clazz);
        if (codec == null) return null;
        return Optional.ofNullable(document.get(key, Document.class))
                .map(encodedDoc -> codec.decode(document.get(key, Document.class).toBsonDocument().asBsonReader(), DecoderContext.builder().build()))
                .orElse(null);
    }

    public static boolean init() {
        if(!System.getenv().containsKey("MONGODB_URI")) {
            Logger.sendError("Cannot find MONGODB_URI inside environment variables!");
            return false;
        }
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

    @Nullable
    public static Document findOne(@NotNull MongoCollection<Document> collection, String key, Object value) {
        return collection.find(Filters.eq(key, value)).first();
    }

    @Nullable
    public static Document findOne(@NotNull MongoCollection<Document> collection, Document searchKey) {
        return collection.find(searchKey).first();
    }

    @NotNull
    public static MongoCursor<Document> findMany(@NotNull MongoCollection<Document> collection, String key, Object value) {
        return collection.find(Filters.eq(key, value)).cursor();
    }

    @NotNull
    public static MongoCursor<Document> findMany(@NotNull MongoCollection<Document> collection, Document searchKey) {
        return collection.find(searchKey).cursor();
    }

    public static boolean deleteOne(@NotNull MongoCollection<Document> collection, String key, Object value) {
        return collection.deleteOne(Filters.eq(key, value)).wasAcknowledged();
    }

    public static boolean deleteOne(@NotNull MongoCollection<Document> collection, Document searchKey) {
        return collection.deleteOne(searchKey).wasAcknowledged();
    }

    public static long deleteMany(@NotNull MongoCollection<Document> collection, String key, Object value) {
        return collection.deleteMany(Filters.eq(key, value)).getDeletedCount();
    }

    public static long deleteMany(@NotNull MongoCollection<Document> collection, Document searchKey) {
        return collection.deleteMany(searchKey).getDeletedCount();
    }

    public static boolean insertOne(@NotNull MongoCollection<Document> collection, Document document) {
        return collection.insertOne(document).wasAcknowledged();
    }

    public static boolean insertMany(@NotNull MongoCollection<Document> collection, Document... documents) {
        return collection.insertMany(Arrays.stream(documents).toList()).wasAcknowledged();
    }

    public static boolean insertMany(@NotNull MongoCollection<Document> collection, List<Document> documents) {
        return collection.insertMany(documents).wasAcknowledged();
    }

    public static boolean updateOneValue(@NotNull MongoCollection<Document> collection, String searchKey, Object searchValue, String updateKey, Object updateValue) {
        return collection.updateOne(Filters.eq(searchKey, searchValue), new Document("$set", new Document(updateKey, updateValue))).wasAcknowledged();
    }

    public static boolean updateOneValue(@NotNull MongoCollection<Document> collection, Document search, String updateKey, Object updateValue) {
        return collection.updateOne(search, new Document("$set", new Document(updateKey, updateValue))).wasAcknowledged();
    }

    public static boolean updateOneValue(@NotNull MongoCollection<Document> collection, String searchKey, Object searchValue, Document update) {
        return collection.updateOne(Filters.eq(searchKey, searchValue), new Document("$set", update)).wasAcknowledged();
    }

    public static boolean updateOneValue(@NotNull MongoCollection<Document> collection, Document search, Document update) {
        return collection.updateOne(search, new Document("$set", update)).wasAcknowledged();
    }

    public static boolean updateManyValue(@NotNull MongoCollection<Document> collection, String searchKey, Object searchValue, String updateKey, Object updateValue) {
        return collection.updateMany(Filters.eq(searchKey, searchValue), new Document("$set", new Document(updateKey, updateValue))).wasAcknowledged();
    }

    public static boolean updateManyValue(@NotNull MongoCollection<Document> collection, Document search, String updateKey, Object updateValue) {
        return collection.updateMany(search, new Document("$set", new Document(updateKey, updateValue))).wasAcknowledged();
    }

    public static boolean updateManyValue(@NotNull MongoCollection<Document> collection, String searchKey, Object searchValue, Document update) {
        return collection.updateMany(Filters.eq(searchKey, searchValue), new Document("$set", update)).wasAcknowledged();
    }

    public static boolean updateManyValue(@NotNull MongoCollection<Document> collection, Document search, Document update) {
        return collection.updateMany(search, new Document("$set", update)).wasAcknowledged();
    }
}
