package pl.dreammc.dreammcapi.velocity.listener;

import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.connection.PostLoginEvent;
import io.lettuce.core.pubsub.StatefulRedisPubSubConnection;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import pl.dreammc.dreammcapi.api.communication.packet.Packet;
import pl.dreammc.dreammcapi.api.logger.Logger;
import pl.dreammc.dreammcapi.api.util.BaseColor;
import pl.dreammc.dreammcapi.shared.Registry;
import pl.dreammc.dreammcapi.velocity.VelocityDreamMCAPI;
import pl.dreammc.dreammcapi.velocity.connection.PostLoginTransferProfileConfirmationPacketListener;
import pl.dreammc.dreammcapi.velocity.manager.ConnectionManager;
import pl.dreammc.dreammcapi.velocity.manager.VelocityProfileManager;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class PlayerPostLoginListener {

    @Subscribe(priority = Short.MIN_VALUE)
    public void onPostLogin(PostLoginEvent event) {
        Logger.sendWarning("Post Login");
        if(!event.getPlayer().isActive() || !VelocityProfileManager.getInstance().isCached(event.getPlayer().getUniqueId()) || Registry.service == null) {
            event.getPlayer().disconnect(Component.text("2Wystąpił problem z ładowaniem profilu. Spróbuj ponownie później.").color(TextColor.fromHexString(BaseColor.redPrimary)));
            return;
        }
        try {
            String channel = Registry.service.getServiceGroup() + ":" + Registry.service.getServiceName() + ":" + Registry.service.getServiceId() + ":PROFILE_TRANSFER_CONFIRMATION:" + event.getPlayer().getUniqueId().toString();
            StatefulRedisPubSubConnection<String, Packet> connection = VelocityDreamMCAPI.getInstance().getRedisConnector().createPrivateConnection();
            var listener = new PostLoginTransferProfileConfirmationPacketListener(channel, event.getPlayer(), connection);
            connection.addListener(listener);
            connection.async().subscribe(channel);
            listener.setTransferRequestModel(ConnectionManager.getInstance().transferPlayer(event.getPlayer(), ConnectionManager.DEFAULT_SERVER));
        } catch (Exception e) {
            event.getPlayer().disconnect(Component.text("5Wystąpił problem z ładowaniem profilu. Spróbuj ponownie później.").color(TextColor.fromHexString(BaseColor.redPrimary)));
            e.printStackTrace();
        }
    }

}
