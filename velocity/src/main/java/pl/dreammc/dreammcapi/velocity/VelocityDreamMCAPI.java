package pl.dreammc.dreammcapi.velocity;

import com.google.inject.Inject;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.plugin.annotation.DataDirectory;
import com.velocitypowered.api.proxy.ProxyServer;
import lombok.Getter;
import org.slf4j.Logger;
import pl.dreammc.dreammcapi.api.communication.RedisConnector;
import pl.dreammc.dreammcapi.api.communication.packet.proxy.RequestAvailableServersPacket;
import pl.dreammc.dreammcapi.api.communication.packet.server.RegisterServerRequestPacket;
import pl.dreammc.dreammcapi.api.communication.packet.server.UnregisterServerRequestPacket;
import pl.dreammc.dreammcapi.api.database.MongoService;
import pl.dreammc.dreammcapi.shared.Registry;
import pl.dreammc.dreammcapi.velocity.config.VelocityAPIConfig;
import pl.dreammc.dreammcapi.velocity.connection.RegisterServerRequestListener;
import pl.dreammc.dreammcapi.velocity.connection.UnregisterServerRequestListener;
import pl.dreammc.dreammcapi.velocity.logger.VelocityLoggerImpl;
import pl.dreammc.dreammcapi.velocity.manager.CommandManager;
import pl.dreammc.dreammcapi.velocity.manager.ConfigManager;
import pl.dreammc.dreammcapi.velocity.player.VelocityMessageSenderImpl;
import pl.dreammc.dreammcapi.velocity.service.VelocityService;

import java.nio.file.Path;


@Plugin(
        id = "dreammc-api",
        name = "DreamMCAPI",
        version = "${version}"
)
public class VelocityDreamMCAPI {

    @Getter private static VelocityDreamMCAPI instance;
    @Getter private final ProxyServer server;
    @Getter private final Logger logger;
    @Getter private final Path dataDirectory;

    //@Getter private ConfigManager configManager;
    //@Getter private VelocityAPIConfig config;
    @Getter private RedisConnector redisConnector;
    @Getter private CommandManager commandManager;

    @Inject
    public VelocityDreamMCAPI(ProxyServer server, Logger logger, @DataDirectory Path dataDirectory) {
        instance = this;
        this.server = server;
        this.logger = logger;
        this.dataDirectory = dataDirectory;
    }

    @Subscribe(priority = Short.MAX_VALUE)
    public void onProxyInitialization(ProxyInitializeEvent event) {
        this.setupAPI();

        if(!MongoService.init()) {
            this.server.shutdown();
            return;
        }

        if(!(this.redisConnector = new RedisConnector()).init()) {
            this.server.shutdown();
            return;
        }
        this.registerRedisListeners();

        this.commandManager = new CommandManager(this.server.getCommandManager());

        this.redisConnector.publish("dreammc:*:*:REQUEST_AVAILABLE_SERVERS", new RequestAvailableServersPacket());
    }

    private void setupAPI() {
        Registry.messageSender = new VelocityMessageSenderImpl();
        Registry.logger = new VelocityLoggerImpl();

        //this.configManager = new ConfigManager();
        //this.config = this.configManager.getOrLoadConfig(this.dataDirectory, VelocityAPIConfig.class);

        new VelocityService();
    }

    private void registerRedisListeners() {
        this.redisConnector.subscribe(new RegisterServerRequestListener(RegisterServerRequestPacket.class));
        this.redisConnector.subscribe(new UnregisterServerRequestListener(UnregisterServerRequestPacket.class));
    }
}
