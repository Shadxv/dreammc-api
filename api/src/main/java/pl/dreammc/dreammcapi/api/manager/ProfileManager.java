package pl.dreammc.dreammcapi.api.manager;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import pl.dreammc.dreammcapi.api.database.MongoService;
import pl.dreammc.dreammcapi.api.model.ProfileModel;

import java.util.UUID;

public class ProfileManager {

    protected final MongoDatabase profileDatabase;
    protected final MongoCollection<Document> profileCollection;

    public ProfileManager() {
        this.profileDatabase = MongoService.getDatabase("dreammcMain");
        this.profileCollection = MongoService.getCollection(this.profileDatabase, "profiles");
    }

    public ProfileModel createNewProfile(UUID uuid) {
        ProfileModel profile = new ProfileModel(uuid);
        MongoService.insertOne(this.profileCollection, profile.toMongoDocument());
        return profile;
    }

}
