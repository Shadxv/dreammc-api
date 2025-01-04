package pl.dreammc.dreammcapi.api.manager;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import lombok.Getter;
import org.bson.Document;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pl.dreammc.dreammcapi.api.communication.RedisConnector;
import pl.dreammc.dreammcapi.api.database.MongoService;
import pl.dreammc.dreammcapi.api.model.ProfileModel;
import pl.dreammc.dreammcapi.api.type.ProfileValueType;

import java.util.UUID;

public class ProfileManager {

    @Getter private static ProfileManager instance;

    protected final MongoDatabase profileDatabase;
    protected final MongoCollection<Document> profileCollection;

    public ProfileManager() {
        instance = this;
        this.profileDatabase = MongoService.getDatabase("dreammcMain");
        this.profileCollection = MongoService.getCollection(this.profileDatabase, "profiles");
    }

    public ProfileModel createNewProfile(UUID uuid) {
        ProfileModel profile = new ProfileModel(uuid);
        MongoService.insertOne(this.profileCollection, profile.toMongoDocument());
        return profile;
    }

    @Nullable
    public ProfileModel findUnloadedProfile(UUID uuid) {
        Document document = MongoService.findOne(this.profileCollection, "uuid", uuid);
        if(document == null) return null;

        return ProfileModel.fromMongoDocument(document);
    }

    // Default - can be overridden
    public void callChangeEvent(UUID playerUUID, ProfileModel profile, ProfileValueType type) {}

    public void sendUpdateToDatabase(UUID playerUUID, String key, Object value) {
        MongoService.updateOneValue(this.profileCollection, "uuid", playerUUID, key, value);
    }
}
