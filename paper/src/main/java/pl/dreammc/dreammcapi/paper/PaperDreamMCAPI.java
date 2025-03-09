package pl.dreammc.dreammcapi.paper;

import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scoreboard.Scoreboard;
import pl.dreammc.dreammcapi.api.communication.RedisConnector;
import pl.dreammc.dreammcapi.api.communication.packet.server.RegisterServerRequestPacket;
import pl.dreammc.dreammcapi.api.communication.packet.server.UnregisterServerRequestPacket;
import pl.dreammc.dreammcapi.api.database.MongoService;
import pl.dreammc.dreammcapi.api.logger.Logger;
import pl.dreammc.dreammcapi.api.manager.PlayerIdManager;
import pl.dreammc.dreammcapi.paper.command.test.ProfileTestCommand;
import pl.dreammc.dreammcapi.paper.connection.RequestAvailableServersListener;
import pl.dreammc.dreammcapi.paper.connection.TransferPlayerRequestPacketListener;
import pl.dreammc.dreammcapi.paper.database.ItemStackCodec;
import pl.dreammc.dreammcapi.paper.logger.PaperLoggerImpl;
import pl.dreammc.dreammcapi.paper.manager.*;
import pl.dreammc.dreammcapi.paper.player.PaperMessageSenderImpl;
import pl.dreammc.dreammcapi.paper.service.PaperService;
import pl.dreammc.dreammcapi.shared.Registry;

public class PaperDreamMCAPI extends JavaPlugin {

    @Getter private static PaperDreamMCAPI instance;
    @Getter private PaperListenerManager listenerManager;
    @Getter private InventoryManager inventoryManager;
    @Getter private HologramManager hologramManager;
    @Getter private NPCManager npcManager;
    @Getter private CommandManager commandManager;
    @Getter private InputManager inputManager;
    @Getter private ScoreboardManager scoreboardManager;
    @Getter private PacketHandlerManager packetHandlerManager;
    @Getter private RedisConnector redisConnector;
    @Getter private TransferManager transferManager;

    @Getter private Scoreboard serverMainScoreboard;

    private boolean isRegistered = false;

    @Override
    public void onLoad() {
        MongoService.registerCodec(new ItemStackCodec());
    }

    @Override
    public void onEnable() {
        instance = this;
        this.setupAPI();

        if(!MongoService.init()) {
            this.getServer().shutdown();
            return;
        }

        if(!(this.redisConnector = new RedisConnector()).init()) {
            this.getServer().shutdown();
            return;
        }
        this.registerRedisListeners();

        this.serverMainScoreboard = Bukkit.getScoreboardManager().getMainScoreboard();

        this.packetHandlerManager = new PacketHandlerManager();
        this.listenerManager = new PaperListenerManager(this);
        this.listenerManager.registerListeners(this.getClassLoader(), "pl.dreammc.dreammcapi.paper.listener");

        this.inventoryManager = new InventoryManager();

        this.hologramManager = new HologramManager();
        this.hologramManager.killPreviousHolograms();

        this.npcManager = new NPCManager();
        this.commandManager = new CommandManager();
        this.inputManager = new InputManager();
        this.scoreboardManager = new ScoreboardManager();
        new PlayerIdManager();
        new PaperProfileManager();
        this.transferManager = new TransferManager();

        this.commandManager.registerCommand(new ProfileTestCommand());

        this.sendRegisterServerRequest("dreammc", "proxy", "*");
        this.isRegistered = true;
    }

    @Override
    public void onDisable() {
        if(this.isRegistered)
            this.sendUnegisterServerRequest("dreammc", "proxy", "*");
        if(this.hologramManager != null)
            this.hologramManager.despawnAll();
    }

    private void setupAPI() {
        Registry.messageSender = new PaperMessageSenderImpl();
        Registry.logger = new PaperLoggerImpl();
        new PaperService();
    }

    private void registerRedisListeners() {
        this.redisConnector.subscribe(new RequestAvailableServersListener());
        this.redisConnector.subscribe(new TransferPlayerRequestPacketListener());
    }

    public void sendRegisterServerRequest(String proxyGroup, String proxyName, String proxyId) {
        String channelBuilder = proxyGroup + ":" + proxyName + ":" + proxyId + ":REGISTER_SERVER";

        String ip = this.getServer().getIp();
        if(ip.isEmpty()) ip = "127.0.0.1";

        int port = this.getServer().getPort();

        PaperDreamMCAPI.getInstance().getRedisConnector().publish(channelBuilder, new RegisterServerRequestPacket(ip, port));
        Logger.sendInfo("Send register request: " + channelBuilder + " | " + ip + ":" + port);
    }

    public void sendUnegisterServerRequest(String proxyGroup, String proxyName, String proxyId) {
        String channelBuilder = proxyGroup + ":" + proxyName + ":" + proxyId + ":UNREGISTER_SERVER";

        String ip = this.getServer().getIp();
        if(ip.isEmpty()) ip = "127.0.0.1";

        int port = this.getServer().getPort();

        PaperDreamMCAPI.getInstance().getRedisConnector().publish(channelBuilder, new UnregisterServerRequestPacket(ip, port));
        Logger.sendInfo("Send unregister request: " + channelBuilder + " | " + ip + ":" + port);
    }
}
