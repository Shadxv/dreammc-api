package pl.dreammc.dreammcapi.shared.player;

import net.kyori.adventure.text.Component;

public interface IMessageSender {

    void sendMessage(Object player, Component message);

}
