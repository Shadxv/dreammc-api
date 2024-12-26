package pl.dreammc.dreammcapi.paper.manager;

import lombok.Getter;
import net.minecraft.network.protocol.Packet;
import net.minecraft.server.packs.repository.Pack;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;
import pl.dreammc.dreammcapi.api.logger.Logger;
import pl.dreammc.dreammcapi.api.util.MessageSender;
import pl.dreammc.dreammcapi.paper.PaperDreamMCAPI;
import pl.dreammc.dreammcapi.paper.packet.PacketEvent;
import pl.dreammc.dreammcapi.paper.packet.PacketHandler;
import pl.dreammc.dreammcapi.paper.packet.PacketListener;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.*;

public class PacketHandlerManager {

    private final Map<Class<? extends Packet<?>>, List<PacketListener<?>>> registeredListeners;
    @Getter private final PacketHandler packetHandler;

    public PacketHandlerManager() {
        this.registeredListeners = new HashMap<>();
        this.packetHandler = new PacketHandler(this);
    }

    public <T extends Packet<?>> void registerListener(Class<? extends Packet<?>> type, PacketListener listener) {
        registeredListeners.computeIfAbsent(type, key -> new ArrayList<>()).add(listener);
    }

    @Nullable
    public <T extends Packet<?>> PacketEvent<T> callEvent(Player player, Packet<?> packet, Class<T> packetClass) {
        try {
            PacketEvent<T> event = new PacketEvent<>(player, (T) packet);

            List<PacketListener<?>> packetListeners = registeredListeners.getOrDefault(packetClass, Collections.emptyList());

            for (PacketListener<?> listener : packetListeners) {
                PacketListener<T> castedListener = (PacketListener<T>) listener;
                castedListener.handlePacket(event);
            }

            return event;
        } catch (ClassCastException e) {
            Logger.sendError("Packet and class does not match type\n" + e.getLocalizedMessage());
            return null;
        }
    }

    public static PacketHandlerManager getInstance() {
        return PaperDreamMCAPI.getInstance().getPacketHandlerManager();
    }

}
