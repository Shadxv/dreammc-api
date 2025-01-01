package pl.dreammc.dreammcapi.velocity.listener;

import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.player.PlayerChooseInitialServerEvent;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import pl.dreammc.dreammcapi.api.logger.Logger;
import pl.dreammc.dreammcapi.api.util.BaseColor;
import pl.dreammc.dreammcapi.shared.Registry;
import pl.dreammc.dreammcapi.velocity.manager.ConnectionManager;
import pl.dreammc.dreammcapi.velocity.manager.VelocityProfileManager;
import pl.dreammc.dreammcapi.velocity.model.TransferRequestModel;
import pl.dreammc.dreammcapi.velocity.type.PlayerTransferStatus;

public class PlayerInitialServerListener {

    @Subscribe(priority = Short.MAX_VALUE)
    public void onChooseServer(PlayerChooseInitialServerEvent event) {
        Logger.sendWarning("Player choose init server");
        TransferRequestModel transferRequestModel = ConnectionManager.getInstance().getTransferRequest(event.getPlayer().getUniqueId());
        if(!event.getPlayer().isActive() || transferRequestModel == null || transferRequestModel.getStatus() != PlayerTransferStatus.PLAYER_ACCEPTED) {
            Logger.sendWarning("Model: " + (transferRequestModel == null ? "null" : transferRequestModel.getTargetServer() + " | " + transferRequestModel.getStatus().name()));
            event.getPlayer().disconnect(Component.text("3Wystąpił problem podczas przenoszenia na serwer docelowy. Spróbuj ponownie później.").color(TextColor.fromHexString(BaseColor.redPrimary)));
            return;
        }

        event.setInitialServer(transferRequestModel.getServerFound());
        ConnectionManager.getInstance().removeTransferRequest(event.getPlayer().getUniqueId());
    }

}
