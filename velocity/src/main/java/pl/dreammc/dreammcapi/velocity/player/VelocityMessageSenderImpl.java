package pl.dreammc.dreammcapi.velocity.player;

import com.velocitypowered.api.proxy.Player;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import pl.dreammc.dreammcapi.shared.player.IMessageSender;

public class VelocityMessageSenderImpl implements IMessageSender {

    @Override
    public void sendMessage(Object receiver, Component message) {
        if(!(receiver instanceof Audience audience)) throw new RuntimeException("Argument cannot receive message (Cast error)");

        audience.sendMessage(message);
    }

}
