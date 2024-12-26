package pl.dreammc.dreammcapi.paper.packet;

import io.netty.channel.Channel;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import net.minecraft.network.protocol.Packet;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import org.bukkit.entity.Player;
import pl.dreammc.dreammcapi.paper.manager.PacketHandlerManager;
import pl.dreammc.dreammcapi.paper.ulit.NMSUtil;

public class PacketHandler {

    private final PacketHandlerManager manager;

    public PacketHandler(PacketHandlerManager manager) {
        this.manager = manager;
    }

    public void injectPacketHandler(Player player) {
        ServerGamePacketListenerImpl connection = NMSUtil.getConnection(player);

        Channel channel = connection.connection.channel;

        ChannelDuplexHandler duplexHandler = new ChannelDuplexHandler() {
            @Override
            public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                if(!(msg instanceof Packet<?> packet)) return;

                PacketEvent<?> event = manager.callEvent(player, packet, packet.getClass());
                if(event != null && event.isCancelled()) return;
                super.channelRead(ctx, msg);
            }
        };

        channel.pipeline().addBefore("packet_handler", player.getName(), duplexHandler);
    }

}
