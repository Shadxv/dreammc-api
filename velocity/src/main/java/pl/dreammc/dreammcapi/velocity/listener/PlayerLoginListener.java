package pl.dreammc.dreammcapi.velocity.listener;

import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.connection.LoginEvent;
import pl.dreammc.dreammcapi.velocity.manager.VelocityProfileManager;

public class PlayerLoginListener {

    @Subscribe(priority = Short.MAX_VALUE)
    public void onLogin(LoginEvent event) {
        VelocityProfileManager.getInstance().loadOrCreateProfile(event.getPlayer().getUniqueId());
    }

}
