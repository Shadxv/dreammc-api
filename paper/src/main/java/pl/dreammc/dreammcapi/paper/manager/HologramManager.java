package pl.dreammc.dreammcapi.paper.manager;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;
import pl.dreammc.dreammcapi.paper.PaperDreamMCAPI;
import pl.dreammc.dreammcapi.paper.hologram.ClientSideHologram;
import pl.dreammc.dreammcapi.paper.hologram.Hologram;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.UUID;

public class HologramManager {

    private final Map<String, Hologram> serverSideHolograms;
    private final Map<UUID, Map<String, ClientSideHologram>> clientSideHolograms;

    public HologramManager() {
        this.serverSideHolograms = new HashMap<>();
        this.clientSideHolograms = new HashMap<>();
    }

    public void registerHologram(Hologram hologram) {
        this.serverSideHolograms.put(hologram.getId(), hologram);
    }

    public void registerHologram(ClientSideHologram hologram) {
        Map<String, ClientSideHologram> playerHolograms;
        if(this.clientSideHolograms.containsKey(hologram.getPlayer().getUniqueId()))
            playerHolograms = this.clientSideHolograms.get(hologram.getPlayer().getUniqueId());
        else {
            playerHolograms = new HashMap<>();
            this.clientSideHolograms.put(hologram.getPlayer().getUniqueId(), playerHolograms);
        }
        playerHolograms.put(hologram.getId(), hologram);
    }

    public void unregisterHologram(String id) {
        this.serverSideHolograms.remove(id);
    }

    public void unregisterHologram(Player player, String id) {
        if(!this.clientSideHolograms.containsKey(player.getUniqueId())) return;
        this.clientSideHolograms.get(player.getUniqueId()).remove(id);
    }

    public void unregisterPlayerHolograms(Player player) {
        this.clientSideHolograms.remove(player.getUniqueId());
    }

    public void despawnAll() {
        Iterator<Hologram> holograms = this.serverSideHolograms.values().iterator();
        while(holograms.hasNext()){
            Hologram hologram = holograms.next();
            hologram.despawn();
        }
        this.serverSideHolograms.clear();
    }

    public void despawnAll(Player player) {
        if(!this.clientSideHolograms.containsKey(player.getUniqueId())) return;
        Iterator<ClientSideHologram> holograms = this.clientSideHolograms.get(player.getUniqueId()).values().iterator();
        while(holograms.hasNext()){
            ClientSideHologram hologram = holograms.next();
            hologram.despawn();
        }
        this.clientSideHolograms.remove(player.getUniqueId());
    }

    @Nullable
    public Hologram getHologram(String id) {
        return this.serverSideHolograms.get(id);
    }

    public static HologramManager getInstance() {
        return PaperDreamMCAPI.getInstance().getHologramManager();
    }

}
