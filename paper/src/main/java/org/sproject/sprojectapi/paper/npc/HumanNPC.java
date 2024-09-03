package org.sproject.sprojectapi.paper.npc;

import com.destroystokyo.paper.profile.PlayerProfile;
import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.protocol.entity.data.EntityData;
import com.github.retrooper.packetevents.protocol.entity.data.EntityDataTypes;
import com.github.retrooper.packetevents.protocol.entity.type.EntityTypes;
import com.github.retrooper.packetevents.protocol.player.GameMode;
import com.github.retrooper.packetevents.protocol.player.TextureProperty;
import com.github.retrooper.packetevents.protocol.player.UserProfile;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerEntityMetadata;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerPlayerInfoUpdate;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerSpawnEntity;
import io.github.retrooper.packetevents.util.SpigotConversionUtil;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.EnumSet;
import java.util.List;

@SuppressWarnings("unchecked")
public abstract class HumanNPC<T extends HumanNPC<?>> extends NPC<T> {

    @Getter protected final int entityId;
    @Getter protected UserProfile gameProfile;

    public HumanNPC(String name, Location location) {
        super(name, location);
        this.entityId = Bukkit.getUnsafe().nextEntityId();
        this.gameProfile = new UserProfile(this.getEntityUUID(), this.getName());
    }

    public T setTexture(String texture, String signature) {
        this.gameProfile.setTextureProperties(List.of(
                new TextureProperty("textures", texture, signature)
        ));
        return (T) this;
    }

    public T setGameProfile(PlayerProfile profile) {
        profile.getProperties().stream().findFirst().ifPresent(property -> {
            this.gameProfile.setTextureProperties(List.of(
                    new TextureProperty("textures", property.getValue(), property.getSignature())
            ));
        });
        return (T) this;
    }

    protected abstract void registerNPC();

    protected void sendSpawnPacket(Player player) {
        var spawnPacket = new WrapperPlayServerSpawnEntity(
                this.entityId,
                this.entityUUID,
                EntityTypes.PLAYER,
                SpigotConversionUtil.fromBukkitLocation(this.spawnLocation),
                0.0f,
                0,
                null
        );

        PacketEvents.getAPI().getPlayerManager().sendPacket(player, spawnPacket);
    }

    protected void sendInfoPacket(Player player) {
        var infoPacket = new WrapperPlayServerPlayerInfoUpdate(
                EnumSet.of(
                        WrapperPlayServerPlayerInfoUpdate.Action.ADD_PLAYER,
                        WrapperPlayServerPlayerInfoUpdate.Action.UPDATE_LISTED
                ),
                new WrapperPlayServerPlayerInfoUpdate.PlayerInfo(
                        this.gameProfile,
                        false,
                        0,
                        GameMode.CREATIVE,
                        null,
                        null
                )
        );

        PacketEvents.getAPI().getPlayerManager().sendPacket(player, infoPacket);
    }

    protected void sendMetadataPacket(Player player) {
        var metadataPacket = new WrapperPlayServerEntityMetadata(
                this.entityId,
                List.of(new EntityData(17, EntityDataTypes.BYTE, Byte.MAX_VALUE))
        );

        PacketEvents.getAPI().getPlayerManager().sendPacket(player, metadataPacket);
    }
}
