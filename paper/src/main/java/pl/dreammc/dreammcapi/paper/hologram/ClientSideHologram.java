package pl.dreammc.dreammcapi.paper.hologram;

import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntList;
import lombok.Getter;
import net.minecraft.network.protocol.game.ClientboundRemoveEntitiesPacket;
import net.minecraft.network.protocol.game.ClientboundSetPassengersPacket;
import net.minecraft.network.protocol.game.ClientboundTeleportEntityPacket;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import net.minecraft.world.entity.Entity;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import pl.dreammc.dreammcapi.paper.hologram.line.ClientSideHologramLine;
import pl.dreammc.dreammcapi.paper.manager.HologramManager;
import pl.dreammc.dreammcapi.paper.ulit.NMSUtil;

import java.util.ArrayList;
import java.util.List;

public class ClientSideHologram extends BaseHologram<ClientSideHologram, ClientSideHologramLine<?>>{

    @Getter private final Player player;
    @Getter private final List<Integer> idsToDespawn;

    public ClientSideHologram(Player player, String id, Location location) {
        super(id, location);
        this.player = player;
        this.idsToDespawn = new ArrayList<>();
        HologramManager.getInstance().registerHologram(this);
    }

    public ClientSideHologram(Player player, String id, Location location, SpawnMode spawnMode) {
        super(id, location, spawnMode);
        this.player = player;
        this.idsToDespawn = new ArrayList<>();
        HologramManager.getInstance().registerHologram(this);
    }

    @Override
    public void spawnLine(ClientSideHologramLine<?> line, int index) {
        Entity currentLine = line.spawn();
        if(index != 0) {
            Entity previousLine = this.getLines().get(index - 1).getEntity();
            previousLine.startRiding(currentLine);
            ServerGamePacketListenerImpl connection = NMSUtil.getConnection(this.player);
            var addPasenger = new ClientboundSetPassengersPacket(previousLine);
            currentLine.teleportRelative(0, this.getLines().get(index).getYTranslation(),0);
            var teleport = new ClientboundTeleportEntityPacket(currentLine);

            connection.send(addPasenger);
            connection.send(teleport);
        }
    }

    @Override
    public void despawn() {
        for (int i = this.getLines().size() - 1; i >= 0; i--) {
            this.getLines().get(i).despawn();
        }
        if(this.idsToDespawn.isEmpty()) return;
        ServerGamePacketListenerImpl connection = NMSUtil.getConnection(this.player);
        IntList convertedList = new IntArrayList(this.idsToDespawn);
        var removeEntities = new ClientboundRemoveEntitiesPacket(convertedList);
        connection.send(removeEntities);
    }
}
