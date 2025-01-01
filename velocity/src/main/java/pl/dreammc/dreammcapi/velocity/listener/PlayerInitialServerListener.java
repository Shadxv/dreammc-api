package pl.dreammc.dreammcapi.velocity.listener;

import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.player.PlayerChooseInitialServerEvent;
import pl.dreammc.dreammcapi.velocity.VelocityDreamMCAPI;
import pl.dreammc.dreammcapi.velocity.event.TransferRequestEvent;
import pl.dreammc.dreammcapi.velocity.manager.ConnectionManager;
import pl.dreammc.dreammcapi.velocity.model.TransferRequestModel;
import pl.dreammc.dreammcapi.velocity.type.PlayerTransferStatus;
import pl.dreammc.dreammcapi.velocity.type.ProfileTransferKickMessage;

import java.util.concurrent.ExecutionException;

public class PlayerInitialServerListener {

    @Subscribe(priority = Short.MAX_VALUE)
    public void onChooseServer(PlayerChooseInitialServerEvent event) {
        if(!event.getPlayer().isActive()) return;

        TransferRequestModel transferRequestModel = ConnectionManager.getInstance().getTransferRequest(event.getPlayer().getUniqueId());

        if(transferRequestModel == null) {
            event.getPlayer().disconnect(ProfileTransferKickMessage.CONNECTION_ERROR.getMessage());
            return;
        }

//        try {
//            boolean result = VelocityDreamMCAPI.getInstance().getServer().getEventManager()
//                    .fire(new TransferRequestEvent(event.getPlayer(), transferRequestModel))
//                    .thenApply(TransferRequestEvent::isCanceled).get();
//            if(result) {
//                if(transferRequestModel.getStatus() != PlayerTransferStatus.DATA_SENT)
//                    transferRequestModel.setStatus(PlayerTransferStatus.TRANSFER_CANCELED);
//                event.getPlayer().disconnect(ProfileTransferKickMessage.PROFILE_NOT_TRANSFERED.getMessage());
//                return;
//            }
//        } catch (InterruptedException | ExecutionException e) {
//            transferRequestModel.setStatus(PlayerTransferStatus.DATA_SENT_TRANSFER_INTERRUPTED);
//            event.getPlayer().disconnect(ProfileTransferKickMessage.PROFILE_NOT_TRANSFERED.getMessage());
//            return;
//        }

        new TransferRequestListener().handleTransferRequest(new TransferRequestEvent(event.getPlayer(), transferRequestModel));


        if(transferRequestModel.getStatus() != PlayerTransferStatus.DATA_SENT) {
            System.out.println(transferRequestModel.getStatus().name());
            event.getPlayer().disconnect(ProfileTransferKickMessage.PROFILE_NOT_TRANSFERED.getMessage());
            return;
        }

        transferRequestModel.setStatus(PlayerTransferStatus.PLAYER_ACCEPTED);
        event.setInitialServer(transferRequestModel.getServerFound());

    }

}
