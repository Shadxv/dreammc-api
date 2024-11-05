package pl.dreammc.dreammcapi.paper.manager;

import org.jetbrains.annotations.Nullable;
import pl.dreammc.dreammcapi.paper.PaperDreamMCAPI;
import pl.dreammc.dreammcapi.paper.hologram.Hologram;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class HologramManager {

    private final Map<String, Hologram> serverSideHolograms;

    public HologramManager() {
        this.serverSideHolograms = new HashMap<>();
    }

    public void registerHologram(Hologram hologram) {
        this.serverSideHolograms.put(hologram.getId(), hologram);
    }

    public void unregisterHologram(String id) {
        this.serverSideHolograms.remove(id);
    }

    public void despawnAll() {
        Iterator<Hologram> holograms = this.serverSideHolograms.values().iterator();
        while(holograms.hasNext()){
            Hologram hologram = holograms.next();
            hologram.despawn();
        }
        this.serverSideHolograms.clear();
    }

    @Nullable
    public Hologram getHologram(String id) {
        return this.serverSideHolograms.get(id);
    }

    public static HologramManager getInstance() {
        return PaperDreamMCAPI.getInstance().getHologramManager();
    }

}
