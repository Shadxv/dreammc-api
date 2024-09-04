package org.sproject.sprojectapi.paper.manager;

import org.jetbrains.annotations.Nullable;
import org.sproject.sprojectapi.paper.PaperSProjectAPI;
import org.sproject.sprojectapi.paper.hologram.BaseHologram;
import org.sproject.sprojectapi.paper.hologram.Hologram;
import org.sproject.sprojectapi.paper.hologram.line.ServerSideHologramLine;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

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
        return PaperSProjectAPI.getInstance().getHologramManager();
    }

}
