package pl.dreammc.dreammcapi.paper.entity;

import com.destroystokyo.paper.event.server.ServerTickEndEvent;
import com.destroystokyo.paper.event.server.ServerTickStartEvent;
import net.minecraft.util.profiling.jfr.event.ServerTickTimeEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import pl.dreammc.dreammcapi.paper.PaperDreamMCAPI;
import pl.dreammc.dreammcapi.paper.manager.PaperListenerManager;

import java.util.HashSet;
import java.util.Set;

public class SmartEntityManager implements Listener {

    private double deltaTime = 0;
    private final Set<SmartEntity> entities;

    public SmartEntityManager() {
        this.entities = new HashSet<>();
        PaperListenerManager.getInstance().registerListener(this);
    }

    public void registerEntity(SmartEntity entity) {
        this.entities.add(entity);
    }

    public void unregisterEntity(SmartEntity entity) {
        this.entities.remove(entity);
    }

    @EventHandler
    public void onTickStart(ServerTickStartEvent event) {
        if (this.deltaTime == 0) return;
        this.entities.forEach(entity -> entity.tick(this.deltaTime));
    }

    @EventHandler
    public void onTickEnd(ServerTickEndEvent event) {
        this.deltaTime = event.getTickDuration() + Math.max(0, event.getTimeRemaining() / 1_000_000);
    }

    public static SmartEntityManager getInstance() {
        return PaperDreamMCAPI.getInstance().getSmartEntityManager();
    }
}
