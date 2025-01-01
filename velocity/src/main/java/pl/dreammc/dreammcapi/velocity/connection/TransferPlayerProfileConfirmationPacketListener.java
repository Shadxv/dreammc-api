package pl.dreammc.dreammcapi.velocity.connection;

import com.velocitypowered.api.proxy.Player;
import pl.dreammc.dreammcapi.api.communication.listener.RedisPacketListener;
import pl.dreammc.dreammcapi.api.communication.packet.server.TransferPlayerProfileConfirmationPacket;
import pl.dreammc.dreammcapi.velocity.VelocityDreamMCAPI;
import pl.dreammc.dreammcapi.velocity.manager.ConnectionManager;
import pl.dreammc.dreammcapi.velocity.model.TransferRequestModel;
import pl.dreammc.dreammcapi.velocity.type.PlayerTransferStatus;

import java.util.Optional;

public class TransferPlayerProfileConfirmationPacketListener extends RedisPacketListener<TransferPlayerProfileConfirmationPacket> {
    public TransferPlayerProfileConfirmationPacketListener() {
        super(TransferPlayerProfileConfirmationPacket.class);
    }

    @Override
    public void handlePacket(TransferPlayerProfileConfirmationPacket packet) {
        Optional<Player> potentailPlayer = VelocityDreamMCAPI.getInstance().getServer().getPlayer(packet.getPlayerUUID());
        if(potentailPlayer.isEmpty())
            return;

        Player player = potentailPlayer.get();
        TransferRequestModel requestModel = ConnectionManager.getInstance().getTransferRequest(packet.getPlayerUUID());
        if(requestModel == null)
            return;
        if(requestModel.getServerFound() == null) {
            ConnectionManager.getInstance().removeTransferRequest(packet.getPlayerUUID());
            return;
        }
        requestModel.setStatus(PlayerTransferStatus.PLAYER_ACCEPTED);
        player.transferToHost(requestModel.getServerFound().getServerInfo().getAddress());
    }
}
