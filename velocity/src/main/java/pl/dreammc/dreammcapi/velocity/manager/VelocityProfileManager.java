package pl.dreammc.dreammcapi.velocity.manager;

import org.bson.Document;
import org.jetbrains.annotations.Nullable;
import pl.dreammc.dreammcapi.api.database.MongoService;
import pl.dreammc.dreammcapi.api.manager.ProfileManager;
import pl.dreammc.dreammcapi.api.model.ProfileModel;
import pl.dreammc.dreammcapi.velocity.VelocityDreamMCAPI;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class VelocityProfileManager extends ProfileManager {

    private final Map<UUID, ProfileModel> cachedProfiles;

    public VelocityProfileManager() {
        super();
        this.cachedProfiles = new HashMap<>();
    }

    public ProfileModel createProfile(UUID uuid) {
        ProfileModel profile = this.createNewProfile(uuid);
        this.cachedProfiles.put(uuid, profile);
        return profile;
    }

    public ProfileModel loadOrCreateProfile(UUID uuid) {
        Document findResult = MongoService.findOne(this.profileCollection, "uuid", uuid);
        if(findResult != null) {
            ProfileModel model = ProfileModel.fromMongoDocument(findResult);
            this.cachedProfiles.put(uuid, model);
            return model;
        }

        return this.createProfile(uuid);
    }

    public boolean isCached(UUID uuid) {
        return this.cachedProfiles.containsKey(uuid);
    }

    @Nullable
    public ProfileModel getAndPopProfile(UUID uuid) {
        return this.cachedProfiles.remove(uuid);
    }

    public void removeProfile(UUID uuid) {
        this.cachedProfiles.remove(uuid);
    }

    public static VelocityProfileManager getInstance() {
        return (VelocityProfileManager) ProfileManager.getInstance();
    }
}
