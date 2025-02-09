package pl.dreammc.dreammcapi.api;

import lombok.Getter;
import pl.dreammc.dreammcapi.api.database.MongoService;
import pl.dreammc.dreammcapi.api.manager.ProfileManager;

public class DreamMCAPI {

    @Getter private static DreamMCAPI instance;

    @Getter private ProfileManager profileManager;

    public DreamMCAPI() {
        instance = this;
    }

    public boolean init() {
        MongoService.init();
        this.profileManager = new ProfileManager();
        return true;
    }



}
