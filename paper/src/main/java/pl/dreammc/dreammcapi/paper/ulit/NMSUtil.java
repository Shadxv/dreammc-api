package pl.dreammc.dreammcapi.paper.ulit;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.network.ServerGamePacketListenerImpl;

import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.craftbukkit.entity.CraftPlayer;
import org.bukkit.entity.Player;

public class NMSUtil {

    private static MinecraftServer server;

    public static String toJSON(Component component) {
        return GsonComponentSerializer.gson().serialize(component);
    }

    public static net.minecraft.network.chat.Component toNMSComponent(Component component) {
        if(server == null) server = ((CraftServer) Bukkit.getServer()).getServer();
        return net.minecraft.network.chat.Component.Serializer.fromJson(toJSON(component), server.registryAccess());
    }

    public static ServerGamePacketListenerImpl getConnection(Player player) {
        return ((CraftPlayer) player).getHandle().connection;
    }
}
