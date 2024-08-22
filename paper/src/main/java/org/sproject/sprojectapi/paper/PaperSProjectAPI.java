package org.sproject.sprojectapi.paper;

import lombok.Getter;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.plugin.java.JavaPlugin;
import org.sproject.sprojectapi.api.database.MongoService;
import org.sproject.sprojectapi.paper.hologram.Hologram;
import org.sproject.sprojectapi.paper.hologram.SpawnMode;
import org.sproject.sprojectapi.paper.hologram.line.TextHologramLine;
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
    public void onEnable() {
        instance = this;
        this.setupAPI();

        if(!MongoService.init()) {
            this.getServer().shutdown();
            return;
        }

        this.listenerManager = new PaperListenerManager(this);
        this.listenerManager.registerListeners();

        this.inventoryManager = new InventoryManager();
        this.hologramManager = new HologramManager();

        new Hologram(
                "test-hologram",
                new Location(Bukkit.getWorld("world"), 0.5, 64, -73.5),
                SpawnMode.FROM_BOTTOM
        ).addLine(new TextHologramLine()
                .setText(Component.text("Test line 1").color(TextColor.fromHexString("#f90055")))
                .setSpacing(0.1f)
        ).addLine(new TextHologramLine()
                .setText(Component.text("Test line 2").color(TextColor.fromHexString("#f58911")))
        ).addLine(new TextHologramLine()
                .setText(Component.text("Test line 3").color(TextColor.fromHexString("#1189f5")))
        ).spawn();
    }

    @Override
    public void onDisable() {
        this.hologramManager.despawnAll();
    }

    private void setupAPI() {
        Registry.messageSender = new PaperMessageSenderImpl();
        Registry.logger = new PaperLoggerImpl();
    }

}
