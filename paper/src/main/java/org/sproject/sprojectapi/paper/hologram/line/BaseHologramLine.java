package org.sproject.sprojectapi.paper.hologram.line;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.Display;
import org.sproject.sprojectapi.paper.hologram.BaseHologram;

import java.util.UUID;

@SuppressWarnings("unchecked")
public abstract class BaseHologramLine<T extends BaseHologramLine<?>> {

    @Setter @Getter protected BaseHologram<?, ?> parrent;
    @Getter protected UUID uuid;
    @Getter protected float height;
    @Getter protected float spacing;
    @Getter protected Display.Billboard billboard;
    @Getter protected float yTranslation;

    protected BaseHologramLine() {
        this.billboard = Display.Billboard.CENTER;
    }

    public abstract T setBillboard(Display.Billboard billboard);

    public T setSpacing(float spacing) {
        this.spacing = spacing;
        return (T) this;
    }

    public abstract void despawn();

}
