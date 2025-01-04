package pl.dreammc.dreammcapi.api.manager;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import lombok.Getter;
import org.bson.Document;
import org.jetbrains.annotations.Nullable;
import pl.dreammc.dreammcapi.api.database.MongoService;

import java.util.UUID;

public class PlayerIdManager {

    @Getter private static PlayerIdManager instance;
    protected final MongoDatabase dataDatabase;
    protected final MongoCollection<Document> dataCollection;

    public PlayerIdManager() {
        instance = this;
        this.dataDatabase = MongoService.getDatabase("dreammcMain");
        this.dataCollection = MongoService.getCollection(this.dataDatabase, "userData");
    }

    @Nullable
    public UUID getPlayerUUID(String name) {
        Document search = MongoService.findOne(this.dataCollection, "username", name);
        if(search == null) return null;
        return search.get("uuid", UUID.class);
    }



}
