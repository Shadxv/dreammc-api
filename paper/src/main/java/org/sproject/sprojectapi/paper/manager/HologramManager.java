package org.sproject.sprojectapi.paper.manager;

import org.jetbrains.annotations.Nullable;
import org.sproject.sprojectapi.paper.PaperSProjectAPI;
import org.sproject.sprojectapi.paper.hologram.BaseHologram;
import org.sproject.sprojectapi.paper.hologram.Hologram;
import org.sproject.sprojectapi.paper.hologram.line.ServerSideHologramLine;

import java.util.HashMap;
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
        for (String id : this.serverSideHolograms.keySet()) {
            Hologram hologram = this.getHologram(id);
            if(hologram == null) return;

            hologram.despawn();
            this.unregisterHologram(id);
        }
    }

    @Nullable
    public Hologram getHologram(String id) {
        return this.serverSideHolograms.get(id);
    }

    public static HologramManager getInstance() {
        return PaperSProjectAPI.getInstance().getHologramManager();
    }

}
