package pl.dreammc.dreammcapi.velocity.manager;

import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.server.RegisteredServer;
import org.jetbrains.annotations.Nullable;
import pl.dreammc.dreammcapi.api.util.MessageSender;
import pl.dreammc.dreammcapi.velocity.VelocityDreamMCAPI;
import pl.dreammc.dreammcapi.velocity.event.TransferRequestEvent;
import pl.dreammc.dreammcapi.velocity.model.TransferRequestModel;
import pl.dreammc.dreammcapi.velocity.type.PlayerTransferStatus;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class ConnectionManager {

    // Temp
    public static final String DEFAULT_SERVER = "lobby-0";

    private final Map<UUID, TransferRequestModel> waitingForTranfer;

    public ConnectionManager() {
        this.waitingForTranfer = new HashMap<>();
    }

    public TransferRequestModel transferPlayer(Player player, String targetServer) {
        @Nullable RegisteredServer currentServer = null;
        if(player.getCurrentServer().isPresent()) currentServer = player.getCurrentServer().get().getServer();

        TransferRequestModel transferRequestModel = new TransferRequestModel(player, targetServer, currentServer);
        Optional<RegisteredServer> searchResult = VelocityDreamMCAPI.getInstance().getServer().getServer(targetServer);
        if(searchResult.isEmpty()) {
            transferRequestModel.setStatus(PlayerTransferStatus.TARGET_NOT_FOUND);
            return transferRequestModel;
        }
        if(searchResult.get().equals(currentServer)) {
            MessageSender.sendErrorMessage(player, "Już znajdujesz się na tym serwerze");
            return transferRequestModel;
        }
        transferRequestModel.setServerFound(searchResult.get());
        transferRequestModel.setStatus(PlayerTransferStatus.TARGET_FOUND);
        this.waitingForTranfer.put(player.getUniqueId(), transferRequestModel);
        return transferRequestModel;
    }

    @Nullable
    public TransferRequestModel getTransferRequest(UUID uuid) {
        return this.waitingForTranfer.get(uuid);
    }

    public void removeTransferRequest(UUID uuid) {
        this.waitingForTranfer.remove(uuid);
    }

    public static ConnectionManager getInstance() {
        return VelocityDreamMCAPI.getInstance().getConnectionManager();
    }
}
