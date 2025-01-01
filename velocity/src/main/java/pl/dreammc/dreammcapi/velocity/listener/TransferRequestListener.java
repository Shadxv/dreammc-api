package pl.dreammc.dreammcapi.velocity.listener;

import com.velocitypowered.api.event.Subscribe;
import pl.dreammc.dreammcapi.api.communication.packet.proxy.TransferPlayerRequestPacket;
import pl.dreammc.dreammcapi.api.communication.packet.shared.TransferPlayerProfilePacket;
import pl.dreammc.dreammcapi.api.model.ProfileModel;
import pl.dreammc.dreammcapi.shared.Registry;
import pl.dreammc.dreammcapi.velocity.VelocityDreamMCAPI;
import pl.dreammc.dreammcapi.velocity.event.TransferRequestEvent;
import pl.dreammc.dreammcapi.velocity.manager.ConnectionManager;
import pl.dreammc.dreammcapi.velocity.manager.VelocityProfileManager;
import pl.dreammc.dreammcapi.velocity.type.PlayerTransferStatus;
import pl.dreammc.dreammcapi.velocity.type.ProfileTransferKickMessage;

public class TransferRequestListener {

    public static void handleTransferRequest(TransferRequestEvent event) {
        if (event.isCanceled()) return;
        if (event.getRequest().getServerFound() == null) {
            event.setCanceled(true);
            ConnectionManager.getInstance().removeTransferRequest(event.getPlayer().getUniqueId());
            return;
        }

        if (event.getRequest().getCurrentServer() == null) {
            if(!handleTransferFromProxy(event)) return;
        } else {
            if(!handleTransferFromServer(event)) return;
        }
        event.getRequest().setStatus(PlayerTransferStatus.DATA_SENT);
    }

    private static boolean handleTransferFromProxy(TransferRequestEvent event) {
        ProfileModel profileModel = VelocityProfileManager.getInstance().getAndPopProfile(event.getPlayer().getUniqueId());
        if(Registry.service == null || profileModel == null) {
            event.setCanceled(true);
            event.getPlayer().disconnect(ProfileTransferKickMessage.PROFILE_NOT_LOADED.getMessage());
            ConnectionManager.getInstance().removeTransferRequest(event.getPlayer().getUniqueId());
            return false;
        }

        String key = Registry.service.getServiceGroup() + ":profile:" + profileModel.getUuid().toString();
        VelocityDreamMCAPI.getInstance().getRedisConnector().sendReactiveCommand(key, new TransferPlayerProfilePacket(profileModel.getUuid(), profileModel), 60).subscribe();
        return true;
    }

    private static boolean handleTransferFromServer(TransferRequestEvent event) {
        if(Registry.service == null) {
            event.setCanceled(true);
            event.getPlayer().sendMessage(ProfileTransferKickMessage.CONNECTION_ERROR.getMessage());
            ConnectionManager.getInstance().removeTransferRequest(event.getPlayer().getUniqueId());
            return false;
        }

        String[] splitedCurrentServerName = event.getRequest().getServerFound().getServerInfo().getName().split("-");
        String currentServerName = splitedCurrentServerName[0];
        String currentServerId = "*";
        if (splitedCurrentServerName.length > 1)
            currentServerId = splitedCurrentServerName[1];
        String channel = Registry.service.getServiceGroup() + ":" + currentServerName + ":" + currentServerId + ":TRANSFER_PLAYER_REQUEST";
        VelocityDreamMCAPI.getInstance().getRedisConnector().publish(channel, new TransferPlayerRequestPacket(event.getPlayer().getUniqueId(), event.getRequest().getTargetServer()));

        return true;
    }
}
