package pl.dreammc.dreammcapi.velocity.listener;

import com.velocitypowered.api.event.Subscribe;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import pl.dreammc.dreammcapi.api.communication.packet.proxy.TransferPlayerProfileRequestFromServerPacket;
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
            ProfileModel profileModel = VelocityProfileManager.getInstance().getAndPopProfile(event.getPlayer().getUniqueId());
            if(Registry.service == null || profileModel == null) {
                event.setCanceled(true);
                event.getPlayer().disconnect(Component.text("1Wystąpił problem podczas transferu danych na serwer docelowy. Spróbuj ponownie za chwilę.").color(TextColor.fromHexString(BaseColor.redPrimary)));
                ConnectionManager.getInstance().removeTransferRequest(event.getPlayer().getUniqueId());
                return;
            }

            String[] splitedName = event.getRequest().getServerFound().getServerInfo().getName().split("-");
            String targetName = splitedName[0];
            String targetId = "*";
            if (splitedName.length > 1)
                targetId = splitedName[1];
            String channel = "dreammc:" + targetName + ":" + targetId + ":PROFILE_TRANSFER_REQUEST";
            event.getRequest().setServerResponse(new CompletableFuture<>());
            VelocityDreamMCAPI.getInstance().getRedisConnector().publish(channel, new TransferPlayerProfilePacket(null, null, event.getPlayer().getUniqueId(), profileModel));
        } else {
            if(Registry.service == null) {
                event.setCanceled(true);
                MessageSender.sendErrorMessage(event.getPlayer(), "6Wystąpił problem podczas zmiany serwera.");
                ConnectionManager.getInstance().removeTransferRequest(event.getPlayer().getUniqueId());
                return;
            }

            String[] splitedCurrentServerName = event.getRequest().getServerFound().getServerInfo().getName().split("-");
            String currentServerName = splitedCurrentServerName[0];
            String currentServerId = "*";
            if (splitedCurrentServerName.length > 1)
                currentServerId = splitedCurrentServerName[1];
            String channel = "dreammc:" + currentServerName + ":" + currentServerId + ":PROFILE_TRANSFER_REQUEST_SERVER";
            String[] splitedTargetName = event.getRequest().getTargetServer().split("-");
            String targetServerName = splitedTargetName[0];
            String targetServerId = "*";
            if (splitedTargetName.length > 1)
                targetServerId = splitedTargetName[1];

            VelocityDreamMCAPI.getInstance().getRedisConnector().publish(channel, new TransferPlayerProfileRequestFromServerPacket(targetServerName, targetServerId, event.getPlayer().getUniqueId()));
        }
        event.getRequest().setStatus(PlayerTransferStatus.PROFILE_TRANSFERED);
    }
}
