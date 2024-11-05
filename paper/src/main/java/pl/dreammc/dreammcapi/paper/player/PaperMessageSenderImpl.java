package pl.dreammc.dreammcapi.paper.player;

import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import pl.dreammc.dreammcapi.shared.player.IMessageSender;

public class PaperMessageSenderImpl  implements IMessageSender {

    @Override
    public void sendMessage(Object player, Component message) {
        if(!(player instanceof Player playerObject)) throw new RuntimeException("Argument is not a player object");

        playerObject.sendMessage(message);
    }

}
