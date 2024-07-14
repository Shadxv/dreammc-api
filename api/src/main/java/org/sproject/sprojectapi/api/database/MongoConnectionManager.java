package org.sproject.sprojectapi.api.database;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import lombok.Getter;
import org.bson.Document;
import org.jetbrains.annotations.Nullable;

public class MongoConnectionManager {

    @Nullable
    @Getter private MongoClient mongoClient;

    public void setupConnection(String uri) {
        this.mongoClient = MongoClients.create(
                MongoClientSettings.builder()
                        .applyConnectionString(new ConnectionString(uri))
                        .build());
    }

    public MongoDatabase getDatabase(String name) {
        if(this.mongoClient == null) throw new RuntimeException("Connection to MongoDB has not been opened!");
        return this.mongoClient.getDatabase(name);
    }

    public MongoCollection<Document> getCollection(MongoDatabase database, String name) {
        return database.getCollection(name);
    }

    public <T> MongoCollection<T> getCollection(MongoDatabase database, String name, Class<T> mapperClass) {
        return database.getCollection(name, mapperClass);
    }

}
