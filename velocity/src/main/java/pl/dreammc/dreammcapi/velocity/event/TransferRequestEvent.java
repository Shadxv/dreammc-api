package pl.dreammc.dreammcapi.velocity.event;

import com.velocitypowered.api.proxy.Player;
import lombok.Getter;
import lombok.Setter;
import pl.dreammc.dreammcapi.velocity.model.TransferRequestModel;

import java.util.UUID;

public class TransferRequestEvent {

    @Getter private final Player player;
    @Getter private final TransferRequestModel request;
    @Getter @Setter private boolean isCanceled;

    public TransferRequestEvent(Player player, TransferRequestModel request) {
        this.player = player;
        this.request =request;
        this.isCanceled = false;
    }

}
