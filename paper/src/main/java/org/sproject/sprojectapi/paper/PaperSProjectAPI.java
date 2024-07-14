package org.sproject.sprojectapi.paper;

import org.bukkit.plugin.java.JavaPlugin;
import org.sproject.sprojectapi.paper.player.PaperMessageSenderImpl;
import org.sproject.sprojectapi.shared.Registry;

public class PaperSProjectAPI extends JavaPlugin {

    @Override
    public void onEnable() {
        this.setupAPI();
    }

    @Override
    public void onDisable() {

    }

    private void setupAPI() {
        Registry.messageSender = new PaperMessageSenderImpl();
    }

}
