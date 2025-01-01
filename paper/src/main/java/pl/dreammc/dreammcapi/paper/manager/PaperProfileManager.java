package pl.dreammc.dreammcapi.paper.manager;

import org.jetbrains.annotations.Nullable;
import pl.dreammc.dreammcapi.api.manager.ProfileManager;
import pl.dreammc.dreammcapi.api.model.ProfileModel;
import pl.dreammc.dreammcapi.paper.PaperDreamMCAPI;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PaperProfileManager extends ProfileManager {

    private final Map<UUID, ProfileModel> playerProfiles;

    public PaperProfileManager() {
        this.playerProfiles = new HashMap<>();
    }

    public void removeProfile(UUID uuid) {
        this.playerProfiles.remove(uuid);
    }

    public void loadProfile(UUID uuid, ProfileModel profileModel) {
        this.playerProfiles.put(uuid, profileModel);
    }

    @Nullable
    public ProfileModel getProfile(UUID uuid) {
        return this.playerProfiles.get(uuid);
    }

    public static PaperProfileManager getInstance() {
        return PaperDreamMCAPI.getInstance().getProfileManager();
    }
}
