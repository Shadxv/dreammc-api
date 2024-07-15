package org.sproject.sprojectapi.velocity;

import com.google.inject.Inject;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.proxy.ProxyServer;
import lombok.Getter;
import org.slf4j.Logger;
import org.sproject.sprojectapi.api.database.MongoService;
import org.sproject.sprojectapi.shared.Registry;
import org.sproject.sprojectapi.velocity.logger.VelocityLoggerImpl;
import org.sproject.sprojectapi.velocity.player.VelocityMessageSenderImpl;


@Plugin(
        id = "sproject-api",
        name = "SProjectAPI",
        version = "${version}"
)
public class VelocitySProjectAPI {

    @Getter private static VelocitySProjectAPI instance;
    @Getter private final ProxyServer server;
    @Getter private final Logger logger;

    @Inject
    public VelocitySProjectAPI(ProxyServer server, Logger logger) {
        this.server = server;
        this.logger = logger;

        instance = this;
    }

    @Subscribe
    public void onProxyInitialization(ProxyInitializeEvent event) {
        this.setupAPI();

        if(!MongoService.init()) {
            this.server.shutdown();
            return;
        }
    }

    private void setupAPI() {
        Registry.messageSender = new VelocityMessageSenderImpl();
        Registry.logger = new VelocityLoggerImpl();
    }
}
