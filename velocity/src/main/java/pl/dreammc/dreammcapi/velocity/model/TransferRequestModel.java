package pl.dreammc.dreammcapi.velocity.model;

import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.server.RegisteredServer;
import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.Nullable;
import pl.dreammc.dreammcapi.velocity.event.TransferRequestEvent;
import pl.dreammc.dreammcapi.velocity.type.PlayerTransferStatus;

@Getter
public class TransferRequestModel {

    private final Player player;
    private final String targetServer;
    @Nullable private final RegisteredServer currentServer;
    @Setter @Nullable private RegisteredServer serverFound;
    @Setter private PlayerTransferStatus status;

    public TransferRequestModel(Player player, String targetServer, @Nullable RegisteredServer currentServer) {
        this.player = player;
        this.targetServer = targetServer;
        this.currentServer = currentServer;
        this.status = PlayerTransferStatus.NOT_STARTED;
    }

}
