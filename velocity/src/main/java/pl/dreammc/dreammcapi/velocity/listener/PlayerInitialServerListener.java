package pl.dreammc.dreammcapi.velocity.listener;

import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.player.PlayerChooseInitialServerEvent;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import pl.dreammc.dreammcapi.api.communication.packet.server.TransferPlayerProfileConfirmationPacket;
import pl.dreammc.dreammcapi.api.logger.Logger;
import pl.dreammc.dreammcapi.api.util.BaseColor;
import pl.dreammc.dreammcapi.shared.Registry;
import pl.dreammc.dreammcapi.velocity.manager.ConnectionManager;
import pl.dreammc.dreammcapi.velocity.manager.VelocityProfileManager;
import pl.dreammc.dreammcapi.velocity.model.TransferRequestModel;
import pl.dreammc.dreammcapi.velocity.type.PlayerTransferStatus;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class PlayerInitialServerListener {

    @Subscribe(priority = Short.MAX_VALUE)
    public void onChooseServer(PlayerChooseInitialServerEvent event) {
        Logger.sendWarning("Player choose init server");
        TransferRequestModel transferRequestModel = ConnectionManager.getInstance().getTransferRequest(event.getPlayer().getUniqueId());

        if(!event.getPlayer().isActive() || transferRequestModel == null) {
            Logger.sendWarning("Model: " + (transferRequestModel == null ? "null" : transferRequestModel.getTargetServer() + " | " + transferRequestModel.getStatus().name()));
            event.getPlayer().disconnect(Component.text("10Wystąpił problem podczas przenoszenia na serwer docelowy. Spróbuj ponownie później.").color(TextColor.fromHexString(BaseColor.redPrimary)));
            return;
        }

        CompletableFuture<TransferPlayerProfileConfirmationPacket> result = transferRequestModel.getServerResponse();
        if(result == null) {
            event.getPlayer().disconnect(Component.text("11Wystąpił problem podczas przenoszenia na serwer docelowy. Spróbuj ponownie później.").color(TextColor.fromHexString(BaseColor.redPrimary)));
            return;
        }

        try {
            TransferPlayerProfileConfirmationPacket packet = result.get(10, TimeUnit.SECONDS);
            if(!packet.isAccepted()) {
                event.getPlayer().disconnect(Component.text("12Wystąpił problem podczas przenoszenia na serwer docelowy. Spróbuj ponownie później.").color(TextColor.fromHexString(BaseColor.redPrimary)));
                return;
            }
            transferRequestModel.setStatus(PlayerTransferStatus.PLAYER_ACCEPTED);
            event.setInitialServer(transferRequestModel.getServerFound());
        } catch (InterruptedException | ExecutionException e) {
            event.getPlayer().disconnect(Component.text("13Wystąpił problem podczas przenoszenia na serwer docelowy. Spróbuj ponownie później.").color(TextColor.fromHexString(BaseColor.redPrimary)));
        } catch (TimeoutException e) {
            event.getPlayer().disconnect(Component.text("Timeout. Spróbuj ponownie później.").color(TextColor.fromHexString(BaseColor.redPrimary)));
        } finally {
            ConnectionManager.getInstance().removeTransferRequest(event.getPlayer().getUniqueId());
        }
    }

}
