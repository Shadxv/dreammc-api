package pl.dreammc.dreammcapi.paper.manager;

import org.bukkit.Bukkit;
import org.jetbrains.annotations.Nullable;
import pl.dreammc.dreammcapi.api.manager.ProfileManager;
import pl.dreammc.dreammcapi.api.model.ProfileModel;
import pl.dreammc.dreammcapi.api.type.ProfileValueType;
import pl.dreammc.dreammcapi.paper.PaperDreamMCAPI;
import pl.dreammc.dreammcapi.paper.event.ProfileValueChangedEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PaperProfileManager extends ProfileManager {

    private final Map<UUID, ProfileModel> playerProfiles;

    public PaperProfileManager() {
        super();
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
        ProfileModel model = this.playerProfiles.get(uuid);
        if(model == null) model = this.findUnloadedProfile(uuid);
        return model;
    }

    @Override
    public void callChangeEvent(UUID playerUUID, ProfileModel profile, ProfileValueType type) {
        Bukkit.getAsyncScheduler().runNow(PaperDreamMCAPI.getInstance(), scheduledTask -> {
            Bukkit.getPluginManager().callEvent(new ProfileValueChangedEvent(playerUUID, profile, type));
        });
    }

    @Override
    public void sendUpdateToDatabase(UUID playerUUID, String key, Object value) {
        Bukkit.getAsyncScheduler().runNow(PaperDreamMCAPI.getInstance(), scheduledTask -> {
            super.sendUpdateToDatabase(playerUUID, key, value);
        });
    }

    public static PaperProfileManager getInstance() {
        return (PaperProfileManager) ProfileManager.getInstance();
    }
}
