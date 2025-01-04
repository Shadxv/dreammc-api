package pl.dreammc.dreammcapi.velocity.listener;

import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.player.KickedFromServerEvent;
import com.velocitypowered.api.proxy.server.RegisteredServer;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import pl.dreammc.dreammcapi.api.util.BaseColor;
import pl.dreammc.dreammcapi.velocity.VelocityDreamMCAPI;
import pl.dreammc.dreammcapi.velocity.manager.ConnectionManager;
import pl.dreammc.dreammcapi.velocity.model.TransferRequestModel;
import pl.dreammc.dreammcapi.velocity.type.ProfileTransferKickMessage;

import java.util.Optional;

public class PlayerKickedListener {

    @Subscribe(priority = Short.MAX_VALUE)
    public void onKick(KickedFromServerEvent event) {
        TransferRequestModel transferRequestModel = ConnectionManager.getInstance().getTransferRequest(event.getPlayer().getUniqueId());

        if(transferRequestModel == null) {
//            Optional<RegisteredServer> searchResult = VelocityDreamMCAPI.getInstance().getServer().getServer(ConnectionManager.DEFAULT_SERVER);
//            if(searchResult.isEmpty()) {
//                return;
//            }
//            event.getServerKickReason().ifPresentOrElse(reason -> {
//                event.setResult(KickedFromServerEvent.RedirectPlayer.create(searchResult.get(), reason));
//            }, () -> {
//                event.setResult(KickedFromServerEvent.RedirectPlayer.create(searchResult.get()));
//            });
            return;
        }

        if(transferRequestModel.getCurrentServer() == null) {
            event.setResult(KickedFromServerEvent.DisconnectPlayer.create(ProfileTransferKickMessage.PROFILE_NOT_TRANSFERED.getMessage()));
        } else {
            Optional<Component> reason = event.getServerKickReason();
            reason.ifPresentOrElse(
                    component -> event.getPlayer().sendMessage(component),
                    () -> event.getPlayer().sendMessage(Component.text("Wystąpił problem podczas zmiany serwera.").color(TextColor.fromHexString(BaseColor.redPrimary)))
            );
            event.setResult(KickedFromServerEvent.RedirectPlayer.create(transferRequestModel.getCurrentServer()));
        }
        ConnectionManager.getInstance().removeTransferRequest(event.getPlayer().getUniqueId());
    }

}
