package pl.dreammc.dreammcapi.paper.hologram.line;

import lombok.Getter;
import net.minecraft.world.entity.Entity;
import pl.dreammc.dreammcapi.paper.hologram.ClientSideHologram;

public abstract class ClientSideHologramLine<T extends ClientSideHologramLine<?>> extends BaseHologramLine<ClientSideHologram, T> {

    @Getter protected Entity entity;

    protected ClientSideHologramLine() {}

    public abstract Entity spawn();

    @Override
    public void despawn() {
        if(this.entity == null) return;
        this.parrent.getIdsToDespawn().add(this.entity.getId());
    }

}
