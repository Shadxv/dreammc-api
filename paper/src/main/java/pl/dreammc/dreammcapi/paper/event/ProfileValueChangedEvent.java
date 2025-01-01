package pl.dreammc.dreammcapi.paper.event;

import lombok.Getter;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;
import pl.dreammc.dreammcapi.api.model.ProfileModel;
import pl.dreammc.dreammcapi.api.type.ProfileValueType;

import java.util.UUID;

public class ProfileValueChangedEvent extends Event {

    private static final HandlerList HANDLER_LIST = new HandlerList();

    @Getter private final UUID playerUUID;
    @Getter private final ProfileModel profile;
    @Getter private final ProfileValueType type;

    public ProfileValueChangedEvent(UUID playerUUID, ProfileModel profile, ProfileValueType type) {
        super(true);
        this.playerUUID = playerUUID;
        this.profile = profile;
        this.type = type;
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return HANDLER_LIST;
    }

    public static HandlerList getHandlerList() {
        return HANDLER_LIST;
    }
}
