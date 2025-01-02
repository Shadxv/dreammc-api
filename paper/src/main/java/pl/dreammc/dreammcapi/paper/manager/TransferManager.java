package pl.dreammc.dreammcapi.paper.manager;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import pl.dreammc.dreammcapi.api.communication.packet.shared.TransferPlayerProfilePacket;
import pl.dreammc.dreammcapi.api.communication.packet.server.TransferPlayerPacket;
import pl.dreammc.dreammcapi.api.logger.Logger;
import pl.dreammc.dreammcapi.api.model.ProfileModel;
import pl.dreammc.dreammcapi.api.util.MessageSender;
import pl.dreammc.dreammcapi.paper.PaperDreamMCAPI;
import pl.dreammc.dreammcapi.shared.Registry;

import java.util.HashMap;
import java.util.Map;

public class TransferManager {

    private final Map<Player, String> transferCache;

    public TransferManager() {
        this.transferCache = new HashMap<>();
    }

    public void transferPlayer(Player player, String targetServer) {
        if(Registry.service == null) return;
        ProfileModel profile = PaperProfileManager.getInstance().getProfile(player.getUniqueId());
        if(profile == null) return;

        this.transferCache.put(player, targetServer);
        Bukkit.getAsyncScheduler().runNow(PaperDreamMCAPI.getInstance(), scheduledTask -> {
            Logger.sendWarning("Transfering player: " + player.getUniqueId());
            PaperDreamMCAPI.getInstance().getRedisConnector().sendReactiveCommand(Registry.service.getServiceGroup() + ":profile:" + player.getUniqueId(), new TransferPlayerProfilePacket(player.getUniqueId(), profile), 60)
                   .doFinally(signalType -> {
                       if(!this.transferCache.containsKey(player)) return;
                       PaperDreamMCAPI.getInstance().getRedisConnector().publish(Registry.service.getServiceGroup() + ":proxy:*:PLAYER_TRANSFER", new TransferPlayerPacket(player.getUniqueId(), targetServer));
                       Logger.sendWarning("Transfer player sent: " + Registry.service.getServiceGroup() + ":proxy:*:PLAYER_TRANSFER");
                   }).doOnError(throwable -> {
                       if(!this.transferCache.containsKey(player) || !player.isOnline()) return;
                       this.transferCache.remove(player);
                       MessageSender.sendErrorMessage(player, "Wystąpił błąd podczas zmiany serwera. Spróbuj ponownie");
                   }).subscribe();
            Logger.sendWarning("Transfer done");
        });
        MessageSender.sendInfoMessage(player, "Przenoszenie na serwer " + targetServer + "...");
    }

    public void removePlayer(Player player) {
        this.transferCache.remove(player);
    }

    public static TransferManager getInstance() {
        return PaperDreamMCAPI.getInstance().getTransferManager();
    }

}
