package pl.dreammc.dreammcapi.velocity;

import com.google.inject.Inject;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.proxy.ProxyServer;
import lombok.Getter;
import org.slf4j.Logger;
import pl.dreammc.dreammcapi.api.database.MongoService;
import pl.dreammc.dreammcapi.shared.Registry;
import pl.dreammc.dreammcapi.velocity.logger.VelocityLoggerImpl;
import pl.dreammc.dreammcapi.velocity.manager.CommandManager;
import pl.dreammc.dreammcapi.velocity.player.VelocityMessageSenderImpl;


@Plugin(
        id = "dreammc-api",
        name = "DreamMCAPI",
        version = "${version}"
)
public class VelocityDreamMCAPI {

    @Getter private static VelocityDreamMCAPI instance;
    @Getter private final ProxyServer server;
    @Getter private final Logger logger;

    @Getter private CommandManager commandManager;

    @Inject
    public VelocityDreamMCAPI(ProxyServer server, Logger logger) {
        instance = this;
        this.server = server;
        this.logger = logger;
    }

    @Subscribe(priority = Short.MAX_VALUE)
    public void onProxyInitialization(ProxyInitializeEvent event) {
        this.setupAPI();

        if(!MongoService.init()) {
            this.server.shutdown();
            return;
        }

        this.commandManager = new CommandManager(this.server.getCommandManager());
    }

    private void setupAPI() {
        Registry.messageSender = new VelocityMessageSenderImpl();
        Registry.logger = new VelocityLoggerImpl();
    }
}
