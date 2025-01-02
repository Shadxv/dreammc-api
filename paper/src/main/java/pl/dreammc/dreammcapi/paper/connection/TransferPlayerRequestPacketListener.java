package pl.dreammc.dreammcapi.paper.connection;

import org.apache.maven.model.Build;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import pl.dreammc.dreammcapi.api.communication.listener.RedisPacketListener;
import pl.dreammc.dreammcapi.api.communication.packet.proxy.TransferPlayerRequestPacket;
import pl.dreammc.dreammcapi.api.logger.Logger;
import pl.dreammc.dreammcapi.paper.manager.TransferManager;

public class TransferPlayerRequestPacketListener extends RedisPacketListener<TransferPlayerRequestPacket> {
    public TransferPlayerRequestPacketListener() {
        super(TransferPlayerRequestPacket.class);
    }

    @Override
    public void handlePacket(TransferPlayerRequestPacket packet) {
        Player player = Bukkit.getPlayer(packet.getPlayerUUID());
        if(player == null) return;
        Logger.sendWarning("Transfer request received: " + packet.getPlayerUUID());
        TransferManager.getInstance().transferPlayer(player, packet.getTargetServer());
    }
}
