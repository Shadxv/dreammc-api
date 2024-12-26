package pl.dreammc.dreammcapi.paper.packet;

import lombok.Getter;
import lombok.Setter;
import net.minecraft.network.protocol.Packet;
import org.bukkit.entity.Player;

public class PacketEvent<T extends Packet<?>> {

    @Getter private final Player player;
    @Getter private final T packet;
    @Getter @Setter private boolean cancelled;

    public PacketEvent(final Player player, final T packet) {
        this.player = player;
        this.packet = packet;
        this.cancelled = false;
    }

}
