package pl.dreammc.dreammcapi.paper.hologram;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.Location;
import pl.dreammc.dreammcapi.api.util.ValueWatcher;
import pl.dreammc.dreammcapi.paper.hologram.line.BaseHologramLine;
import pl.dreammc.dreammcapi.paper.hologram.line.TextHologramLine;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("unchecked")
public abstract class BaseHologram<T extends BaseHologram<?, ?>, V extends BaseHologramLine<T, ?>> {

    @Getter private final String id;
    @Getter private Location location;
    @Getter private SpawnMode spawnMode;
    @Getter private final List<V> lines;
    @Getter private ValueWatcher<Float> height;
    @Getter private boolean canSeeThrough = false;
    @Getter @Setter private boolean isSpawned = false;

    protected BaseHologram(final String id, Location location) {
        this.id = id;
        this.location = location;
        this.spawnMode = SpawnMode.FROM_BOTTOM;
        this.height = new ValueWatcher<>(0f);
        this.lines = new ArrayList<>();
    }

    protected BaseHologram(final String id, Location location, SpawnMode spawnMode) {
        this.id = id;
        this.location = location;
        this.spawnMode = spawnMode;
        this.height = new ValueWatcher<>(0f);
        this.lines = new ArrayList<>();
    }

    public T addLine(V line) {
        int index = 0;
        line.setParrent((T) this);
        this.lines.add(index, line);
        if(isSpawned) this.spawnLine(line, index);
        return (T) this;
    }

    public T setCanSeeThrough(boolean canSeeThrough) {
        this.canSeeThrough = canSeeThrough;

        if(isSpawned) {
            for(BaseHologramLine<?, ?> line : this.lines) {
                if(line instanceof TextHologramLine textLine) {
                    textLine.setCanSeeThrough(canSeeThrough);
                }
            }
        }

        return (T) this;
    }

    public void spawnLine(int index) {
        this.spawnLine(lines.get(index), index);
    }

    public abstract void spawnLine(V line, int index);

    public void spawn() {
        for(int i = 0; i < this.lines.size(); i++) {
            this.spawnLine(i);
        }
        this.isSpawned = true;
    }

    public void despawn() {
        for (int i = this.lines.size() - 1; i >= 0; i--) {
            this.lines.get(i).despawn();
        }
    }
}
