package pl.dreammc.dreammcapi.velocity.listener;

import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.connection.PostLoginEvent;
import pl.dreammc.dreammcapi.api.logger.Logger;
import pl.dreammc.dreammcapi.shared.Registry;
import pl.dreammc.dreammcapi.velocity.manager.ConnectionManager;
import pl.dreammc.dreammcapi.velocity.manager.VelocityProfileManager;
import pl.dreammc.dreammcapi.velocity.type.PlayerTransferStatus;
import pl.dreammc.dreammcapi.velocity.type.ProfileTransferKickMessage;

public class PlayerPostLoginListener {

    @Subscribe(priority = Short.MIN_VALUE)
    public void onPostLogin(PostLoginEvent event) {
        Logger.sendWarning("Post Login");
        if(!event.getPlayer().isActive() || !VelocityProfileManager.getInstance().isCached(event.getPlayer().getUniqueId()) || Registry.service == null) {
            event.getPlayer().disconnect(ProfileTransferKickMessage.PROFILE_NOT_LOADED.getMessage());
            return;
        }
        ConnectionManager.getInstance().transferPlayer(event.getPlayer(), ConnectionManager.DEFAULT_SERVER).getStatus();
    }

}
