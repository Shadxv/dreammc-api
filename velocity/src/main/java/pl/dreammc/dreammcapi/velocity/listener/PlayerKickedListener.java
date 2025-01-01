package pl.dreammc.dreammcapi.velocity.listener;

import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.player.KickedFromServerEvent;
import pl.dreammc.dreammcapi.velocity.manager.ConnectionManager;
import pl.dreammc.dreammcapi.velocity.model.TransferRequestModel;
import pl.dreammc.dreammcapi.velocity.type.ProfileTransferKickMessage;

public class PlayerKickedListener {

    @Subscribe(priority = Short.MAX_VALUE)
    public void onKick(KickedFromServerEvent event) {
        TransferRequestModel transferRequestModel = ConnectionManager.getInstance().getTransferRequest(event.getPlayer().getUniqueId());

        if(transferRequestModel != null && transferRequestModel.getCurrentServer() == null) {
            event.setResult(KickedFromServerEvent.DisconnectPlayer.create(ProfileTransferKickMessage.PROFILE_NOT_TRANSFERED.getMessage()));
        }
    }

}
