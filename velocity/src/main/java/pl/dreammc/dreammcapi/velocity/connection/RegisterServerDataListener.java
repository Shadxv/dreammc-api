package pl.dreammc.dreammcapi.velocity.connection;

import com.velocitypowered.api.proxy.server.ServerInfo;
import pl.dreammc.dreammcapi.api.communication.listener.RedisPacketListener;
import pl.dreammc.dreammcapi.api.communication.packet.server.RegisterServerDataPacket;
import pl.dreammc.dreammcapi.api.communication.packet.server.RegisterServerRequestPacket;
import pl.dreammc.dreammcapi.api.logger.Logger;
import pl.dreammc.dreammcapi.velocity.VelocityDreamMCAPI;

import java.net.InetSocketAddress;

public class RegisterServerDataListener extends RedisPacketListener<RegisterServerDataPacket> {

    public RegisterServerDataListener() {
        super(RegisterServerDataPacket.class);
    }

    @Override
    public void handlePacket(RegisterServerDataPacket packet) {
        InetSocketAddress address = new InetSocketAddress(packet.getAddressIP(), packet.getPort());

        VelocityDreamMCAPI.getInstance().getServer().registerServer(new ServerInfo(packet.getServiceId(), address));
        Logger.sendInfo("Received register request: " + packet.getServiceId() + " | " + packet.getAddressIP() + ":" + packet.getPort());
    }
}
