package pl.dreammc.dreammcapi.paper.communication;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import pl.dreammc.dreammcapi.api.communication.listener.RedisPacketListener;
import pl.dreammc.dreammcapi.api.util.BaseColor;

public class SendMessageListener extends RedisPacketListener<SendMessagePacket> {

    public SendMessageListener() {
        super(SendMessagePacket.class);
    }

    @Override
    public void handlePacket(SendMessagePacket packet) {
        for(Player player : Bukkit.getOnlinePlayers()) {
            player.sendMessage(Component.text(packet.getPlayerName() + " said: ").color(TextColor.fromHexString(BaseColor.greenPrimary)).append(Component.text(packet.getMessage()).color(TextColor.fromHexString(BaseColor.grayPrimary))));
        }
    }
}
