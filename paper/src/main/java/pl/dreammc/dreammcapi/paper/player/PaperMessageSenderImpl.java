package pl.dreammc.dreammcapi.paper.player;

import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import pl.dreammc.dreammcapi.shared.player.IMessageSender;

public class PaperMessageSenderImpl  implements IMessageSender {

    @Override
    public void sendMessage(Object receiver, Component message) {
        if(!(receiver instanceof Audience audience)) throw new RuntimeException("Argument cannot receive message (Cast error)");

        audience.sendMessage(message);
    }

}
