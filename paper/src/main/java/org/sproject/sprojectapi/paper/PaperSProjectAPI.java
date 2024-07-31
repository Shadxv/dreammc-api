package org.sproject.sprojectapi.paper;

import lombok.Getter;
import org.bukkit.plugin.java.JavaPlugin;
import org.sproject.sprojectapi.api.database.MongoService;
import org.sproject.sprojectapi.paper.logger.PaperLoggerImpl;
import org.sproject.sprojectapi.paper.manager.InventoryManager;
import org.sproject.sprojectapi.paper.player.PaperMessageSenderImpl;
import org.sproject.sprojectapi.shared.Registry;

public class PaperSProjectAPI extends JavaPlugin {

    @Getter private static PaperSProjectAPI instance;
    @Getter private InventoryManager inventoryManager;

    @Override
    public void onEnable() {
        instance = this;
        this.setupAPI();

        if(!MongoService.init()) {
            this.getServer().shutdown();
            return;
        }

        this.inventoryManager = new InventoryManager();
    }

    @Override
    public void onDisable() {

    }

    private void setupAPI() {
        Registry.messageSender = new PaperMessageSenderImpl();
        Registry.logger = new PaperLoggerImpl();
    }

}
