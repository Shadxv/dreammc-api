package pl.dreammc.dreammcapi.paper.entity.ai;

import org.jetbrains.annotations.NotNull;
import pl.dreammc.dreammcapi.paper.entity.SmartEntity;

public abstract class Goal {

    protected SmartEntity entity;

    public void setEntity(@NotNull SmartEntity entity) {
        this.entity = entity;
    }

    public abstract boolean tick(double deltaTime);

}
