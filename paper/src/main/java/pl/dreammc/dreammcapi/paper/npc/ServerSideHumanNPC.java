package pl.dreammc.dreammcapi.paper.npc;

import lombok.Getter;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;
import pl.dreammc.dreammcapi.paper.hologram.BaseHologram;
import pl.dreammc.dreammcapi.paper.hologram.Hologram;
import pl.dreammc.dreammcapi.paper.manager.HologramManager;
import pl.dreammc.dreammcapi.paper.manager.NPCManager;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

public class ServerSideHumanNPC extends HumanNPC<ServerSideHumanNPC>{

    @Getter private final List<Player> viewers;
    @Getter @Nullable private Hologram hologram;

    public ServerSideHumanNPC(Location location) {
        super(location);
        this.viewers = new ArrayList<>();
    }

    @Override
    protected boolean registerNPC() {
        if(!NPCManager.getInstance().registerNPC(this)) return false;
        if(!NPCManager.getInstance().registerServerSideNPC(this)) return false;
        return true;
    }

    @Override
    protected void unregisterNPC() {
        NPCManager.getInstance().unregisterServerSideNPC(this);
        NPCManager.getInstance().unregisterNPC(this);
    }

    public void addViewer(Player player) {
        if(!this.spawnLocation.getWorld().getName().equals(player.getWorld().getName())) return;
        if(this.viewers.contains(player)) return;
        if(!player.isOnline()) return;
        this.viewers.add(player);
        this.spawn(player);
    }

    public void removeViewer(Player player) {
        if(!this.viewers.contains(player)) return;
        this.viewers.remove(player);
        if(!player.isOnline()) return;
        this.despawn(player);
    }

    public Hologram addOrGetHologram() {
        if(this.hologram != null) return this.hologram;
        this.hologram = new Hologram(this.name, this.currentLocation.clone().add(0, 2.2, 0));
        return this.hologram;
    }

    public ServerSideHumanNPC removeHologram() {
        if(this.hologram == null) return this;
        HologramManager.getInstance().unregisterHologram(this.hologram.getId());
        if(this.hologram.isSpawned())
            this.hologram.despawn();
        this.hologram = null;
        return this;
    }

    private void spawn(Player player) {
        this.sendInfoPacket(player);
        this.sendSpawnPacket(player);
        this.sendMetadataPacket(player);
        this.sendHideNicknamePackets(player);
    }

    private void despawn(Player player) {
        this.sendDespawnPackets(player);
    }

    @Override
    public ServerSideHumanNPC spawn() {
        if(!this.registerNPC()) return this;

        for(Player player : this.viewers) {
            this.spawn(player);
        }

        if(this.hologram != null) this.hologram.spawn();

        this.isSpawned = true;
        return this;
    }

    @Override
    public ServerSideHumanNPC despawn() {
        this.unregisterNPC();

        for (Player player : viewers) {
            this.removeViewer(player);
        }

        Optional.ofNullable(this.hologram).ifPresent(BaseHologram::despawn);

        this.isSpawned = false;
        return this;
    }

    @Override
    public void move(double deltaX, double deltaY, double deltaZ) {
        this.viewers.forEach(player -> {
            this.move(player, deltaX, deltaY, deltaZ);
        });
    }

    @Override
    public void move(double deltaX, double deltaY, double deltaZ, float yaw, float pitch) {
        this.viewers.forEach(player -> {
            this.move(player, deltaX, deltaY, deltaZ, yaw, pitch);
        });
    }

    @Override
    public void rotate(float yaw, float pitch) {
        this.viewers.forEach(player -> this.rotate(player, yaw, pitch));
    }

    @Override
    public void playAnimation() {
        this.viewers.forEach(this::playAnimation);
    }

    @Override
    public void attack() {
        this.viewers.forEach(this::attack);
    }

    @Override
    public @Nullable Entity findClosestEntity(double radius) {
        return this.currentLocation.getNearbyPlayers(radius)
                .stream()
                .min(Comparator.comparingDouble(p -> p.getLocation().distanceSquared(this.currentLocation)))
                .orElse(null);
    }
}
