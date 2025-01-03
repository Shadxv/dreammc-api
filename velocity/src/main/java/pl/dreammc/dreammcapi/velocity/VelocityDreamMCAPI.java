package pl.dreammc.dreammcapi.velocity;

import com.google.inject.Inject;
import com.velocitypowered.api.command.CommandMeta;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.plugin.annotation.DataDirectory;
import com.velocitypowered.api.proxy.ProxyServer;
import lombok.Getter;
import org.slf4j.Logger;
import pl.dreammc.dreammcapi.api.communication.RedisConnector;
import pl.dreammc.dreammcapi.api.communication.packet.proxy.RequestAvailableServersPacket;
import pl.dreammc.dreammcapi.api.database.MongoService;
import pl.dreammc.dreammcapi.shared.Registry;
import pl.dreammc.dreammcapi.velocity.command.proxy.ServerCommand;
import pl.dreammc.dreammcapi.velocity.connection.RegisterServerRequestListener;
import pl.dreammc.dreammcapi.velocity.connection.TransferPlayerPacketListener;
import pl.dreammc.dreammcapi.velocity.connection.UnregisterServerRequestListener;
import pl.dreammc.dreammcapi.velocity.listener.*;
import pl.dreammc.dreammcapi.velocity.logger.VelocityLoggerImpl;
import pl.dreammc.dreammcapi.velocity.manager.*;
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
    @Getter private ServerGroupManager serverGroupManager;
    @Getter private CommandManager commandManager;
    @Getter private ConnectionManager connectionManager;

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

        this.serverGroupManager = new ServerGroupManager();
        this.commandManager = new CommandManager(this.server.getCommandManager());
        new VelocityProfileManager();
        this.connectionManager = new ConnectionManager();

        this.registerListeners();

        ServerCommand command = new ServerCommand();
        CommandMeta.Builder builder = this.server.getCommandManager().metaBuilder(command.getName());
        for (String alias : command.getAliases()) {
            builder.aliases(alias);
        }
        builder.plugin(this);
        this.server.getCommandManager().register(builder.build(), command);

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
        this.redisConnector.subscribe(new RegisterServerRequestListener());
        this.redisConnector.subscribe(new UnregisterServerRequestListener());
        this.redisConnector.subscribe(new TransferPlayerPacketListener());
    }

    private void registerListeners() {
        this.server.getEventManager().register(this, new PlayerDisconnectListener());
        this.server.getEventManager().register(this, new PlayerInitialServerListener());
        this.server.getEventManager().register(this, new PlayerLoginListener());
        this.server.getEventManager().register(this, new PlayerPostLoginListener());
        this.server.getEventManager().register(this, new PlayerTransferListener());
        this.server.getEventManager().register(this, new PlayerKickedListener());
    }

    private void sendRequestForAllAvailableServers() {
        if(Registry.service == null) return;
        this.redisConnector.publish(Registry.service.getServiceGroup() + ":*:*:REQUEST_AVAILABLE_SERVERS", new RequestAvailableServersPacket());
    }
}
