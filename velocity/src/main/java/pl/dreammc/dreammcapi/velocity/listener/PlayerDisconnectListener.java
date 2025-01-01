package pl.dreammc.dreammcapi.velocity.listener;

import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.connection.DisconnectEvent;
import pl.dreammc.dreammcapi.velocity.manager.ConnectionManager;
import pl.dreammc.dreammcapi.velocity.manager.VelocityProfileManager;
import pl.dreammc.dreammcapi.velocity.model.TransferRequestModel;
import pl.dreammc.dreammcapi.velocity.type.PlayerTransferStatus;

public class PlayerDisconnectListener {

    @Subscribe(priority = Short.MIN_VALUE)
    public void onDisconnect(DisconnectEvent event) {
        TransferRequestModel transferRequestModel = ConnectionManager.getInstance().getTransferRequest(event.getPlayer().getUniqueId());
        if(transferRequestModel != null && transferRequestModel.getStatus().ordinal() > PlayerTransferStatus.TRANSFER_CANCELED.ordinal()) {
            String[] splitedTargetName = transferRequestModel.getTargetServer().split("-");
            String targetServerName = splitedTargetName[0];
            String targetServerId = "*";
            if (splitedTargetName.length > 1)
                targetServerId = splitedTargetName[1];
            String targetChannel = "dreammc:" + targetServerName + ":" + targetServerId + ":REMOVE_TRANSFER_DATA";

            //TODO: add packet later
        }



        ConnectionManager.getInstance().removeTransferRequest(event.getPlayer().getUniqueId());
        VelocityProfileManager.getInstance().removeProfile(event.getPlayer().getUniqueId());
    }

}
