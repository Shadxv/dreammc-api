package pl.dreammc.dreammcapi.paper.ulit;

import io.papermc.paper.adventure.PaperAdventure;
import net.kyori.adventure.text.Component;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import org.bukkit.craftbukkit.entity.CraftPlayer;
import org.bukkit.entity.Player;

public class NMSUtil {

    public static net.minecraft.network.chat.Component toNMSComponent(Component component) {
        return PaperAdventure.asVanilla(component);
    }

    public static ServerGamePacketListenerImpl getConnection(Player player) {
        return ((CraftPlayer) player).getHandle().connection;
    }
}
