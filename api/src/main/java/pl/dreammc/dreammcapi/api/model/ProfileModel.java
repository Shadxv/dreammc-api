package pl.dreammc.dreammcapi.api.model;

import lombok.Getter;

import java.util.UUID;

public class ProfileModel {

    @Getter private final UUID uniqueId;
    @Getter private final String name;

    public ProfileModel(UUID uniqueId, String name) {
        this.uniqueId = uniqueId;
        this.name = name;
    }
}
