package pl.dreammc.dreammcapi.velocity.listener;

import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.connection.DisconnectEvent;
import pl.dreammc.dreammcapi.api.communication.RedisConnector;
import pl.dreammc.dreammcapi.shared.Registry;
import pl.dreammc.dreammcapi.velocity.VelocityDreamMCAPI;
import pl.dreammc.dreammcapi.velocity.manager.ConnectionManager;
import pl.dreammc.dreammcapi.velocity.manager.VelocityProfileManager;
import pl.dreammc.dreammcapi.velocity.model.TransferRequestModel;
import pl.dreammc.dreammcapi.velocity.type.PlayerTransferStatus;

public class PlayerDisconnectListener {

    @Subscribe(priority = Short.MIN_VALUE)
    public void onDisconnect(DisconnectEvent event) {
        TransferRequestModel transferRequestModel = ConnectionManager.getInstance().getTransferRequest(event.getPlayer().getUniqueId());
        if(Registry.service != null && transferRequestModel != null && transferRequestModel.getStatus().ordinal() > PlayerTransferStatus.TRANSFER_CANCELED.ordinal()) {
            String key = Registry.service.getServiceGroup() + ":profile:" + transferRequestModel.getPlayer().getUniqueId();
            VelocityDreamMCAPI.getInstance().getRedisConnector().deleteReactiveCommand(key);
        }

        ConnectionManager.getInstance().removeTransferRequest(event.getPlayer().getUniqueId());
        VelocityProfileManager.getInstance().removeProfile(event.getPlayer().getUniqueId());
    }

}
