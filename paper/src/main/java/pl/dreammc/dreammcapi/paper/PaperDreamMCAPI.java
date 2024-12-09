package pl.dreammc.dreammcapi.paper;

import com.github.retrooper.packetevents.PacketEvents;
import io.github.retrooper.packetevents.factory.spigot.SpigotPacketEventsBuilder;
import lombok.Getter;
import org.apache.maven.model.Build;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scoreboard.Scoreboard;
import pl.dreammc.dreammcapi.api.database.MongoService;
import pl.dreammc.dreammcapi.paper.database.ItemStackCodec;
import pl.dreammc.dreammcapi.paper.logger.PaperLoggerImpl;
import pl.dreammc.dreammcapi.paper.manager.*;
import pl.dreammc.dreammcapi.paper.player.PaperMessageSenderImpl;
import pl.dreammc.dreammcapi.shared.Registry;

public class PaperDreamMCAPI extends JavaPlugin {

    @Getter private static PaperDreamMCAPI instance;
    @Getter private PaperListenerManager listenerManager;
    @Getter private InventoryManager inventoryManager;
    @Getter private HologramManager hologramManager;
    @Getter private NPCManager npcManager;
    @Getter private CommandManager commandManager;
    @Getter private InputManager inputManager;

    @Getter private Scoreboard serverMainScoreboard;

    @Override
    public void onLoad() {
        PacketEvents.setAPI(SpigotPacketEventsBuilder.build(this));
        PacketEvents.getAPI().load();

        MongoService.registerCodec(new ItemStackCodec());
    }

    @Override
    public void onEnable() {
        instance = this;
        this.setupAPI();

        PacketEvents.getAPI().init();

        if(!MongoService.init()) {
            this.getServer().shutdown();
            return;
        }

        this.serverMainScoreboard = Bukkit.getScoreboardManager().getMainScoreboard();

        this.listenerManager = new PaperListenerManager(this);
        this.listenerManager.registerListeners();

        this.inventoryManager = new InventoryManager();
        this.hologramManager = new HologramManager();
        this.npcManager = new NPCManager();
        this.commandManager = new CommandManager();
        this.inputManager = new InputManager();
    }

    @Override
    public void onDisable() {
        this.hologramManager.despawnAll();
        PacketEvents.getAPI().terminate();
    }

    private void setupAPI() {
        Registry.messageSender = new PaperMessageSenderImpl();
        Registry.logger = new PaperLoggerImpl();
    }

}
