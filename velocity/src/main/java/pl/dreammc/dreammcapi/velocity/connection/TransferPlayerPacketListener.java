package pl.dreammc.dreammcapi.velocity.connection;

import com.velocitypowered.api.proxy.Player;
import pl.dreammc.dreammcapi.api.communication.listener.RedisPacketListener;
import pl.dreammc.dreammcapi.api.communication.packet.server.TransferPlayerPacket;
import pl.dreammc.dreammcapi.api.logger.Logger;
import pl.dreammc.dreammcapi.api.util.MessageSender;
import pl.dreammc.dreammcapi.velocity.VelocityDreamMCAPI;
import pl.dreammc.dreammcapi.velocity.manager.ConnectionManager;
import pl.dreammc.dreammcapi.velocity.model.TransferRequestModel;
import pl.dreammc.dreammcapi.velocity.type.PlayerTransferStatus;

import java.util.Optional;

public class TransferPlayerPacketListener extends RedisPacketListener<TransferPlayerPacket> {
    public TransferPlayerPacketListener() {
        super(TransferPlayerPacket.class);
    }

    @Override
    public void handlePacket(TransferPlayerPacket packet) {
        Optional<Player> potentailPlayer = VelocityDreamMCAPI.getInstance().getServer().getPlayer(packet.getPlayerUUID());
        if(potentailPlayer.isEmpty())
            return;

        Player player = potentailPlayer.get();
        TransferRequestModel transferRequestModel = ConnectionManager.getInstance().getTransferRequest(packet.getPlayerUUID());
        if(transferRequestModel == null)
            transferRequestModel = ConnectionManager.getInstance().transferPlayer(player, packet.getTargetServer());
        if(transferRequestModel.getServerFound() == null) {
            MessageSender.sendErrorMessage(player, "Nie znaleziono serwera docelowego. Spróbuj ponownie później.");
            ConnectionManager.getInstance().removeTransferRequest(player.getUniqueId());
            return;
        }
        Logger.sendWarning("Transfering player...");
        player.createConnectionRequest(transferRequestModel.getServerFound()).fireAndForget();
    }
}
