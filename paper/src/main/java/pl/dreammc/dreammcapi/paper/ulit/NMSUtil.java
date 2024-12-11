package pl.dreammc.dreammcapi.paper.ulit;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import org.bukkit.craftbukkit.v1_20_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

public class NMSUtil {

    public static String toJSON(Component component) {
        return GsonComponentSerializer.gson().serialize(component);
    }

    public static net.minecraft.network.chat.Component toNMSComponent(Component component) {
        return net.minecraft.network.chat.Component.Serializer.fromJson(toJSON(component));
    }

    public static ServerGamePacketListenerImpl getConnection(Player player) {
        return ((CraftPlayer) player).getHandle().connection;
    }
}
