package pl.dreammc.dreammcapi.paper.packet;

import net.minecraft.network.protocol.Packet;

public interface PacketListener<T extends Packet<?>> {
   void handlePacket(PacketEvent<T> event);
}
