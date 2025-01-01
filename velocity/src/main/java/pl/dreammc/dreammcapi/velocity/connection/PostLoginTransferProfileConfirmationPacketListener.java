package pl.dreammc.dreammcapi.velocity.connection;

import com.velocitypowered.api.proxy.Player;
import io.lettuce.core.pubsub.StatefulRedisPubSubConnection;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import pl.dreammc.dreammcapi.api.communication.listener.RedisPacketListener;
import pl.dreammc.dreammcapi.api.communication.packet.Packet;
import pl.dreammc.dreammcapi.api.communication.packet.server.TransferPlayerProfileConfirmationPacket;
import pl.dreammc.dreammcapi.api.logger.Logger;
import pl.dreammc.dreammcapi.api.util.BaseColor;
import pl.dreammc.dreammcapi.velocity.manager.ConnectionManager;
import pl.dreammc.dreammcapi.velocity.model.TransferRequestModel;
import pl.dreammc.dreammcapi.velocity.type.PlayerTransferStatus;

import java.util.concurrent.CountDownLatch;

public class PostLoginTransferProfileConfirmationPacketListener extends RedisPacketListener<TransferPlayerProfileConfirmationPacket> {

    private TransferRequestModel transferRequestModel;
    private final String channel;
    private final Player player;
    private final StatefulRedisPubSubConnection<String, Packet> connection;
    private final CountDownLatch latch;

    public PostLoginTransferProfileConfirmationPacketListener(String channel, Player player, StatefulRedisPubSubConnection<String, Packet> connection, CountDownLatch latch) {
        super(TransferPlayerProfileConfirmationPacket.class);
        this.channel = channel;
        this.player = player;
        this.connection = connection;
        this.latch = latch;
    }

    @Override
    public void handlePacket(TransferPlayerProfileConfirmationPacket packet) {}

    @Override
    public void message(String channel, Packet message) {
        Logger.sendError("Received message from: " + channel);
        if(!(message instanceof TransferPlayerProfileConfirmationPacket packet)) {
            return;
        }

        if(!channel.equals(this.channel)) return;

        TransferRequestModel requestModel = ConnectionManager.getInstance().getTransferRequest(packet.getPlayerUUID());
        if(requestModel == null || requestModel.getStatus() != PlayerTransferStatus.PROFILE_TRANSFERED) {
            this.player.disconnect(Component.text("4Wystąpił problem z ładowaniem profilu. Spróbuj ponownie później. " + (requestModel == null ? "null" : requestModel.getStatus())).color(TextColor.fromHexString(BaseColor.redPrimary)));
            ConnectionManager.getInstance().removeTransferRequest(packet.getPlayerUUID());
        } else {
            requestModel.setStatus(PlayerTransferStatus.PLAYER_ACCEPTED);
            Logger.sendWarning("Player accepted");
        }
        latch.countDown();
        connection.async().unsubscribe(channel);
        connection.close();
    }

    @Override
    public void subscribed(String channel, long count) {
        this.transferRequestModel = ConnectionManager.getInstance().transferPlayer(this.player, ConnectionManager.DEFAULT_SERVER);
        super.subscribed(channel, count);
    }
}
