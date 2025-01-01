package pl.dreammc.dreammcapi.velocity.listener;

import com.velocitypowered.api.event.Subscribe;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import pl.dreammc.dreammcapi.api.communication.packet.proxy.TransferPlayerProfilePacket;
import pl.dreammc.dreammcapi.api.model.ProfileModel;
import pl.dreammc.dreammcapi.api.util.BaseColor;
import pl.dreammc.dreammcapi.api.util.MessageSender;
import pl.dreammc.dreammcapi.shared.Registry;
import pl.dreammc.dreammcapi.velocity.VelocityDreamMCAPI;
import pl.dreammc.dreammcapi.velocity.event.TransferRequestEvent;
import pl.dreammc.dreammcapi.velocity.manager.ConnectionManager;
import pl.dreammc.dreammcapi.velocity.manager.VelocityProfileManager;
import pl.dreammc.dreammcapi.velocity.type.PlayerTransferStatus;
import pl.dreammc.dreammcapi.velocity.type.ProfileTransferKickMessage;
import reactor.core.publisher.Mono;

import java.util.concurrent.CompletableFuture;

public class TransferRequestListener {

    @Subscribe(priority = Short.MAX_VALUE)
    public void handleTransferRequest(TransferRequestEvent event) {
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

    private boolean handleTransferFromProxy(TransferRequestEvent event) {
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

    private boolean handleTransferFromServer(TransferRequestEvent event) {
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
        String channel = "dreammc:" + currentServerName + ":" + currentServerId + ":PROFILE_TRANSFER_REQUEST_SERVER";

        return true;
    }
}
