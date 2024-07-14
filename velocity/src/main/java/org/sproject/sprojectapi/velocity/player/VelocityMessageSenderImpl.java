package org.sproject.sprojectapi.velocity.player;

import com.velocitypowered.api.proxy.Player;
import net.kyori.adventure.text.Component;
import org.sproject.sprojectapi.shared.player.IMessageSender;

public class VelocityMessageSenderImpl implements IMessageSender {

    @Override
    public void sendMessage(Object player, Component message) {
        if(!(player instanceof Player playerObject)) throw new RuntimeException("Argument is not a player object");

        playerObject.sendMessage(message);
    }

}
