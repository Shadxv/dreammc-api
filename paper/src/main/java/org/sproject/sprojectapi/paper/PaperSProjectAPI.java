package org.sproject.sprojectapi.paper;

import com.github.retrooper.packetevents.PacketEvents;
import io.github.retrooper.packetevents.factory.spigot.SpigotPacketEventsBuilder;
import lombok.Getter;
import org.bukkit.plugin.java.JavaPlugin;
import org.sproject.sprojectapi.api.database.MongoService;
import org.sproject.sprojectapi.paper.logger.PaperLoggerImpl;
import org.sproject.sprojectapi.paper.manager.HologramManager;
import org.sproject.sprojectapi.paper.manager.InventoryManager;
import org.sproject.sprojectapi.paper.manager.PaperListenerManager;
import org.sproject.sprojectapi.paper.player.PaperMessageSenderImpl;
import org.sproject.sprojectapi.shared.Registry;

public class PaperSProjectAPI extends JavaPlugin {

    @Getter private static PaperSProjectAPI instance;
    @Getter private PaperListenerManager listenerManager;
    @Getter private InventoryManager inventoryManager;
    @Getter private HologramManager hologramManager;

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
