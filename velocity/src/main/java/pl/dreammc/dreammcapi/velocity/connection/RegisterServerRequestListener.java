package pl.dreammc.dreammcapi.velocity.connection;

import com.velocitypowered.api.proxy.server.ServerInfo;
import pl.dreammc.dreammcapi.api.communication.listener.RedisPacketListener;
import pl.dreammc.dreammcapi.api.communication.packet.server.RegisterServerRequestPacket;
import pl.dreammc.dreammcapi.api.logger.Logger;
import pl.dreammc.dreammcapi.velocity.VelocityDreamMCAPI;

import java.net.InetSocketAddress;

public class RegisterServerRequestListener extends RedisPacketListener<RegisterServerRequestPacket> {

    public RegisterServerRequestListener(Class<RegisterServerRequestPacket> packetClass) {
        super(packetClass);
    }

    @Override
    public void handlePacket(RegisterServerRequestPacket packet) {
        InetSocketAddress address = new InetSocketAddress(packet.getAddress(), packet.getPort());
        String name = packet.getSenderServiceName() + "-" + packet.getSenderServiceId();

        VelocityDreamMCAPI.getInstance().getServer().registerServer(new ServerInfo(name, address));
        VelocityDreamMCAPI.getInstance().getServer().getConfiguration().getAttemptConnectionOrder().add(name);
        String channelBuilder = packet.getSenderServiceGroup() + ":" + packet.getSenderServiceName() + ":" + packet.getSenderServiceId() + ":REGISTER_SERVER";
        Logger.sendInfo("Received register request: " + channelBuilder + " | " + name + " | " + packet.getAddress() + ":" + packet.getPort());
    }
}
