package pl.dreammc.dreammcapi.paper;

import com.github.retrooper.packetevents.PacketEvents;
import io.github.retrooper.packetevents.factory.spigot.SpigotPacketEventsBuilder;
import lombok.Getter;
import org.bukkit.plugin.java.JavaPlugin;
import pl.dreammc.dreammcapi.api.database.MongoService;
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

    @Override
    public void onLoad() {
        PacketEvents.setAPI(SpigotPacketEventsBuilder.build(this));
        PacketEvents.getAPI().load();
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
