package pl.dreammc.dreammcapi.paper.hologram.line;

import lombok.Getter;
import org.bukkit.entity.Display;
import org.bukkit.entity.Entity;
import pl.dreammc.dreammcapi.paper.hologram.Hologram;

@SuppressWarnings("unchecked")
public abstract class ServerSideHologramLine<T extends ServerSideHologramLine<?>> extends BaseHologramLine<Hologram, T> {

    @Getter protected Entity entity;

    protected ServerSideHologramLine() {}

    public abstract Entity spawn();

    @Override
    public T setBillboard(Display.Billboard billboard) {
        this.billboard = billboard;
        if(!(entity instanceof Display display)) return (T) this;
        display.setBillboard(billboard);
        return (T) this;
    }

    @Override
    public void despawn() {
        this.entity.remove();
    }
}
