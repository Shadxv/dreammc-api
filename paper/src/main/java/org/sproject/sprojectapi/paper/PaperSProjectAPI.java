package org.sproject.sprojectapi.paper;

import lombok.Getter;
import org.bukkit.plugin.java.JavaPlugin;
import org.sproject.sprojectapi.api.database.MongoService;
import org.sproject.sprojectapi.paper.logger.PaperLoggerImpl;
import org.sproject.sprojectapi.paper.player.PaperMessageSenderImpl;
import org.sproject.sprojectapi.shared.Registry;

public class PaperSProjectAPI extends JavaPlugin {

    @Getter private static PaperSProjectAPI instance;

    @Override
    public void onEnable() {
        instance = this;
        this.setupAPI();

        if(!MongoService.init()) {
            this.getServer().shutdown();
            return;
        }
    }

    @Override
    public void onDisable() {

    }

    private void setupAPI() {
        //test
        Registry.messageSender = new PaperMessageSenderImpl();
        Registry.logger = new PaperLoggerImpl();
    }

}
