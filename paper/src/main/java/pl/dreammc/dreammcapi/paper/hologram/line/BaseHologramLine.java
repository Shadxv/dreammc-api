package pl.dreammc.dreammcapi.paper.hologram.line;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.Display;
import pl.dreammc.dreammcapi.paper.hologram.BaseHologram;

import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;

@SuppressWarnings("unchecked")
public abstract class BaseHologramLine<T extends BaseHologramLine<?>> {

    @Setter @Getter protected BaseHologram<?, ?> parrent;
    @Getter protected UUID uuid;
    @Getter protected float height;
    @Getter protected float spacing;
    @Getter protected Display.Billboard billboard = Display.Billboard.CENTER;
    @Getter protected float yTranslation;
    protected boolean isSpawned = false;

    protected BaseHologramLine() {}

    public abstract T setBillboard(Display.Billboard billboard);

    public T setSpacing(float spacing) {
        this.spacing = spacing;
        return (T) this;
    }

    protected boolean isHologramSpawned() {
        AtomicBoolean isSpawned = new AtomicBoolean(false);
        Optional.ofNullable(this.parrent).ifPresent(parrent -> {
            isSpawned.set(parrent.isSpawned());
        });
        return isSpawned.get();
    }

    public abstract void despawn();

}
