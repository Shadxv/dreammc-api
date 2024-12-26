package pl.dreammc.dreammcapi.api.manager;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import pl.dreammc.dreammcapi.api.database.MongoService;

public class ProfileManager {

    private final MongoDatabase profileDatabase;
    private final MongoCollection<Document> profileCollection;

    public ProfileManager() {
        this.profileDatabase = MongoService.getDatabase("dreammcMain");
        this.profileCollection = MongoService.getCollection(this.profileDatabase, "profiles");
    }



}
