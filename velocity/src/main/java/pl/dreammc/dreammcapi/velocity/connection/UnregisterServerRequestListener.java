package pl.dreammc.dreammcapi.velocity.connection;

import com.velocitypowered.api.proxy.server.ServerInfo;
import pl.dreammc.dreammcapi.api.communication.listener.RedisPacketListener;
import pl.dreammc.dreammcapi.api.communication.packet.server.UnregisterServerRequestPacket;
import pl.dreammc.dreammcapi.api.logger.Logger;
import pl.dreammc.dreammcapi.velocity.VelocityDreamMCAPI;

import java.net.InetSocketAddress;

public class UnregisterServerRequestListener extends RedisPacketListener<UnregisterServerRequestPacket> {

    public UnregisterServerRequestListener() {
        super(UnregisterServerRequestPacket.class);
    }

    @Override
    public void handlePacket(UnregisterServerRequestPacket packet) {
        InetSocketAddress address = new InetSocketAddress(packet.getAddress(), packet.getPort());
        String name = packet.getSenderServiceName() + "-" + packet.getSenderServiceId();

        VelocityDreamMCAPI.getInstance().getServer().unregisterServer(new ServerInfo(name, address));
        String channelBuilder = packet.getSenderServiceGroup() + ":" + packet.getSenderServiceName() + ":" + packet.getSenderServiceId() + ":UNREGISTER_SERVER";
        Logger.sendInfo("Received unregister request: " + channelBuilder + " | " + name + " | " + packet.getAddress() + ":" + packet.getPort());

    }
}
