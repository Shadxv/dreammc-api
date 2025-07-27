package pl.dreammc.dreammcapi.paper.npc;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import lombok.Getter;
import net.minecraft.network.protocol.game.*;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.GameType;
import net.minecraft.world.phys.Vec3;
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

    @Getter protected GameProfile gameProfile;
    @Getter protected boolean isSpawned;

    public HumanNPC(Location location) {
        super(generateName(Bukkit.getUnsafe().nextEntityId()), location);
        this.entityId = extractIdFromName(this.getName());
        this.gameProfile = new GameProfile(this.getEntityUUID(), this.getName().substring(Math.max(0, name.length() - 17)));
    }

    public T setTexture(String texture, String signature) {
        this.gameProfile.getProperties().put("textures", new Property("textures", texture, signature));
        return (T) this;
    }

    public T setGameProfile(GameProfile profile) {
        profile.getProperties().get("textures").stream().findFirst().ifPresent(property -> {
            this.gameProfile.getProperties().put("textures", property);
        });
        return (T) this;
    }

    protected abstract boolean registerNPC();

    protected abstract void unregisterNPC();

    protected void sendSpawnPacket(Player player) {
        ServerGamePacketListenerImpl connection = NMSUtil.getConnection(player);

        var spanwPacket = new ClientboundAddEntityPacket(
                this.entityId,
                this.entityUUID,
                this.spawnLocation.getX(),
                this.spawnLocation.getY(),
                this.spawnLocation.getZ(),
                this.spawnLocation.getPitch(),
                this.spawnLocation.getYaw(),
                EntityType.PLAYER,
                0,
                Vec3.ZERO,
                this.spawnLocation.getYaw()
        );

        connection.send(spanwPacket);
    }

    protected void sendInfoPacket(Player player) {
        ServerGamePacketListenerImpl connection = NMSUtil.getConnection(player);

        var infoPacket = new ClientboundPlayerInfoUpdatePacket(
                EnumSet.of(
                        ClientboundPlayerInfoUpdatePacket.Action.ADD_PLAYER,
                        ClientboundPlayerInfoUpdatePacket.Action.UPDATE_LISTED
                ),
                new ClientboundPlayerInfoUpdatePacket.Entry(
                        this.gameProfile.getId(),
                        this.gameProfile,
                        false,
                        0,
                        GameType.CREATIVE,
                        null,
                        true,
                        -1,
                        null
                )
        );

        connection.send(infoPacket);
    }

    protected void sendMetadataPacket(Player player) {
        ServerGamePacketListenerImpl connection = NMSUtil.getConnection(player);

        var metadataPacket = new ClientboundSetEntityDataPacket(
                this.entityId,
                List.of(new SynchedEntityData.DataValue<>(17, EntityDataSerializers.BYTE, Byte.MAX_VALUE))
        );

        connection.send(metadataPacket);
    }

    protected void sendDespawnPackets(Player player) {
        ServerGamePacketListenerImpl connection = NMSUtil.getConnection(player);

        var removeEntityPacket = new ClientboundRemoveEntitiesPacket(this.entityId);
        var removeInfoPacket = new ClientboundPlayerInfoRemovePacket(List.of(this.entityUUID));

        connection.send(removeEntityPacket);
        connection.send(removeInfoPacket);
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
