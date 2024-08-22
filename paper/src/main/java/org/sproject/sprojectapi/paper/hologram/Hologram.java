package org.sproject.sprojectapi.paper.hologram;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.sproject.sprojectapi.paper.hologram.line.ServerSideHologramLine;
import org.sproject.sprojectapi.paper.manager.HologramManager;

public class Hologram extends BaseHologram<Hologram, ServerSideHologramLine<?>> {

    public Hologram(String id, Location location) {
        super(id, location);
        HologramManager.getInstance().registerHologram(this);
    }

    public Hologram(String id, Location location, SpawnMode spawnMode) {
        super(id, location, spawnMode);
        HologramManager.getInstance().registerHologram(this);
    }

    @Override
    public void spawnLine(ServerSideHologramLine<?> line, int index) {
        Entity currentLine = line.spawn();
        if(index != 0) {
            Entity previousLine = this.getLines().get(index - 1).getEntity();
            previousLine.addPassenger(currentLine);
            currentLine.teleport(currentLine.getLocation().clone().add(0, this.getLines().get(index).getYTranslation(), 0));
        }
    }
}
