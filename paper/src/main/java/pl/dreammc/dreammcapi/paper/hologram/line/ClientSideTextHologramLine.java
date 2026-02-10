package pl.dreammc.dreammcapi.paper.hologram.line;

import lombok.Getter;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import net.minecraft.network.protocol.game.ClientboundSetEntityDataPacket;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerEntity;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.phys.Vec3;
import org.bukkit.Color;
import org.bukkit.craftbukkit.CraftWorld;
import org.bukkit.entity.Display;
import org.bukkit.entity.TextDisplay;
import org.bukkit.util.Transformation;
import org.joml.Vector3f;
import pl.dreammc.dreammcapi.api.util.TextUtil;
import pl.dreammc.dreammcapi.paper.ulit.NMSUtil;

import java.util.List;
import java.util.Optional;

public class ClientSideTextHologramLine extends ClientSideHologramLine<ClientSideTextHologramLine> {

    @Getter private Component text;
    @Getter private TextDisplay.TextAlignment textAlignment;
    @Getter private Color backgroundColor;
    @Getter private boolean defaultBackground = true;
    @Getter private boolean shadowed = false;
    @Getter private byte textOpacity = -1;
    @Getter private float scale = 1.0f;
    @Getter private int lines;
    private static final float DEFAULT_HEIGHT = 0.275f;

    public ClientSideTextHologramLine() {}

    public ClientSideTextHologramLine setText(Component text) {
        this.text = text;
        if(this.isSpawned) {
            ServerGamePacketListenerImpl connection = NMSUtil.getConnection(this.getParrent().getPlayer());
            var updateText = new ClientboundSetEntityDataPacket(this.entity.getId(), List.of(new SynchedEntityData.DataValue<>(23, EntityDataSerializers.COMPONENT, NMSUtil.toNMSComponent(this.text))));
            connection.send(updateText);
        }
        this.lines = TextUtil.countLines(text, 200);
        return this;
    }

//    public ClientSideTextHologramLine setTextAlignment(TextDisplay.TextAlignment textAlignment) {
//        this.textAlignment = textAlignment;
//        if(this.isSpawned) {
//            ServerGamePacketListenerImpl connection = NMSUtil.getConnection(this.getParrent().getPlayer());
//            var updateAlignment = new ClientboundSetEntityDataPacket(this.entity.getId(), List.of(new SynchedEntityData.DataValue<>(27, EntityDataSerializers.BYTE, textAlignmentByte(this.textAlignment))));
//            connection.send(updateAlignment);
//        }
//        return this;
//    }

    @Override
    public Entity spawn() {
        this.yTranslation = this.parrent.getHeight().get();

        this.entity = new net.minecraft.world.entity.Display.TextDisplay(
                EntityType.TEXT_DISPLAY,
                ((CraftWorld)this.getParrent().getLocation().getWorld()).getHandle()
        );
        this.entity.setPos(this.parrent.getLocation().getX(), this.parrent.getLocation().getY(), this.parrent.getLocation().getZ());
        this.entity.setRot(this.parrent.getLocation().getYaw(), this.parrent.getLocation().getPitch());

        ServerGamePacketListenerImpl connection = NMSUtil.getConnection(this.parrent.getPlayer());

        var spawnLinePacket = new ClientboundAddEntityPacket(
                this.entity.getId(),
                this.entity.getUUID(),
                this.entity.getX(),
                this.entity.getY(),
                this.entity.getZ(),
                this.parrent.getLocation().getPitch(),
                this.parrent.getLocation().getYaw(),
                this.entity.getType(),
                0,
                Vec3.ZERO,
                0
        );

        var updateText = new ClientboundSetEntityDataPacket(
                this.entity.getId(),
                List.of(new SynchedEntityData.DataValue<>(23, EntityDataSerializers.COMPONENT, NMSUtil.toNMSComponent(this.text)))
        );

        connection.send(spawnLinePacket);
        connection.send(updateText);
        this.sendBillboardPacket();

        float heightChange = DEFAULT_HEIGHT  * this.lines - this.height;
        this.height = DEFAULT_HEIGHT * this.lines;
        this.parrent.getHeight().set(this.parrent.getHeight().get() + heightChange + this.spacing);

        this.isSpawned = true;
        return this.entity;
    }

    @Override
    public ClientSideTextHologramLine setBillboard(Display.Billboard billboard) {
        this.billboard = billboard;
        if(this.isSpawned) this.sendBillboardPacket();
        return this;
    }

    private void sendBillboardPacket() {
        var metadataPacket = new ClientboundSetEntityDataPacket(
                this.entity.getId(),
                List.of(new SynchedEntityData.DataValue<>(15, EntityDataSerializers.BYTE, (byte) this.billboard.ordinal()))
        );
        NMSUtil.getConnection(this.parrent.getPlayer()).send(metadataPacket);
    }

    private static byte textAlignmentByte(TextDisplay.TextAlignment textAlignment) {
        int alignment = textAlignment.ordinal();
        byte metaData = 0x00;
        metaData |= (byte) (alignment << 3);
        return metaData;
    }
}
