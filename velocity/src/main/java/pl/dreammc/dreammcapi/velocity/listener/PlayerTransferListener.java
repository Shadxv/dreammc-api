package pl.dreammc.dreammcapi.velocity.listener;

import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.player.ServerConnectedEvent;
import pl.dreammc.dreammcapi.velocity.manager.ConnectionManager;

public class PlayerTransferListener {

    @Subscribe(priority = Short.MAX_VALUE)
    public void onTransfer(ServerConnectedEvent event) {
        ConnectionManager.getInstance().removeTransferRequest(event.getPlayer().getUniqueId());
    }

}
