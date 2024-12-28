package pl.dreammc.dreammcapi.paper;

import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scoreboard.Scoreboard;
import pl.dreammc.dreammcapi.api.communication.RedisConnector;
import pl.dreammc.dreammcapi.api.database.MongoService;
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

    @Getter private Scoreboard serverMainScoreboard;

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

        if((this.redisConnector = new RedisConnector()).init()) {
            this.getServer().shutdown();
            return;
        }

        this.serverMainScoreboard = Bukkit.getScoreboardManager().getMainScoreboard();

        this.packetHandlerManager = new PacketHandlerManager();
        this.listenerManager = new PaperListenerManager(this);
        this.listenerManager.registerListeners();

        this.inventoryManager = new InventoryManager();

        this.hologramManager = new HologramManager();
        this.hologramManager.killPreviousHolograms();

        this.npcManager = new NPCManager();
        this.commandManager = new CommandManager();
        this.inputManager = new InputManager();
        this.scoreboardManager = new ScoreboardManager();
    }

    @Override
    public void onDisable() {
        this.hologramManager.despawnAll();
    }

    private void setupAPI() {
        Registry.messageSender = new PaperMessageSenderImpl();
        Registry.logger = new PaperLoggerImpl();
        new PaperService(this.getConfig());
    }

}
