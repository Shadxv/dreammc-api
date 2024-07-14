package org.sproject.sprojectapi.velocity;

import com.google.inject.Inject;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.proxy.Player;
import org.slf4j.Logger;
import org.sproject.sprojectapi.shared.Registry;
import org.sproject.sprojectapi.velocity.player.VelocityMessageSenderImpl;


@Plugin(
        id = "sproject-api",
        name = "SProjectAPI",
        version = "${version}"
)
public class VelocitySProjectAPI {

    @Inject private Logger logger;

    @Subscribe
    public void onProxyInitialization(ProxyInitializeEvent event) {
        this.setupAPI();
    }

    private void setupAPI() {
        Registry.messageSender = new VelocityMessageSenderImpl();
    }
}
