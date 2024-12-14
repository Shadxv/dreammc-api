package pl.dreammc.dreammcapi.paper.npc;

import com.destroystokyo.paper.profile.PlayerProfile;
import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.protocol.entity.data.EntityData;
import com.github.retrooper.packetevents.protocol.entity.data.EntityDataTypes;
import com.github.retrooper.packetevents.protocol.entity.type.EntityTypes;
import com.github.retrooper.packetevents.protocol.player.GameMode;
import com.github.retrooper.packetevents.protocol.player.TextureProperty;
import com.github.retrooper.packetevents.protocol.player.UserProfile;
import com.github.retrooper.packetevents.wrapper.play.server.*;
import io.github.retrooper.packetevents.util.SpigotConversionUtil;
import lombok.Getter;
import net.minecraft.network.protocol.game.ClientboundSetPlayerTeamPacket;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import net.minecraft.world.scores.PlayerTeam;
import net.minecraft.world.scores.Team;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.scoreboard.CraftScoreboard;
import org.bukkit.entity.Player;
import pl.dreammc.dreammcapi.paper.PaperDreamMCAPI;
import pl.dreammc.dreammcapi.paper.ulit.NMSUtil;

import java.util.EnumSet;
import java.util.List;

@SuppressWarnings("unchecked")
public abstract class HumanNPC<T extends HumanNPC<?>> extends NPC<T> {

    @Getter protected UserProfile gameProfile;
    @Getter protected boolean isSpawned;

    public HumanNPC(Location location) {
        super(generateName(Bukkit.getUnsafe().nextEntityId()), location);
        this.entityId = extractIdFromName(this.getName());
        this.gameProfile = new UserProfile(this.getEntityUUID(), this.getName().substring(Math.max(0, name.length() - 17)));
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

    protected abstract boolean registerNPC();

    protected abstract void unregisterNPC();

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

        var headRotationPacket = new WrapperPlayServerEntityHeadLook(this.entityId, this.spawnLocation.getYaw());

        PacketEvents.getAPI().getPlayerManager().sendPacket(player, spawnPacket);
        PacketEvents.getAPI().getPlayerManager().sendPacket(player, headRotationPacket);
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

    protected void sendDespawnPackets(Player player) {
        var removeEntityPacket = new WrapperPlayServerDestroyEntities(this.entityId);
        var removeInfoPacket = new WrapperPlayServerPlayerInfoRemove(this.entityUUID);

        PacketEvents.getAPI().getPlayerManager().sendPacket(player, removeEntityPacket);
        PacketEvents.getAPI().getPlayerManager().sendPacket(player, removeInfoPacket);
    }

    protected void sendHideNicknamePackets(Player player) {
        ServerGamePacketListenerImpl connection = NMSUtil.getConnection(player);
        PlayerTeam team = new PlayerTeam(((CraftScoreboard) PaperDreamMCAPI.getInstance().getServerMainScoreboard()).getHandle(), this.getName().substring(Math.max(this.getName().length() - 16, 0)));
        team.setNameTagVisibility(Team.Visibility.NEVER);

        connection.send(ClientboundSetPlayerTeamPacket.createAddOrModifyPacket(team, false));
        connection.send(ClientboundSetPlayerTeamPacket.createAddOrModifyPacket(team, true));
        connection.send(ClientboundSetPlayerTeamPacket.createPlayerPacket(team, this.name, ClientboundSetPlayerTeamPacket.Action.ADD));
    }

    private static String generateName(int id) {
        return "NPCID_" + id;
    }

    private static int extractIdFromName(String name) {
        return Integer.parseInt(name.replace("NPCID_", "").trim());
    }
}
