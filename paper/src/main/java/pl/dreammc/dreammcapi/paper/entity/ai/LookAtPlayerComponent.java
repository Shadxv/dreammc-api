package pl.dreammc.dreammcapi.paper.entity.ai;

import org.bukkit.entity.Entity;
import org.bukkit.util.Vector;

public class LookAtPlayerComponent extends Goal {

    private final double maxDistance;

    public LookAtPlayerComponent(double maxDistance) {
        this.maxDistance = maxDistance;
    }

    @Override
    public boolean tick(double deltaTime) {
        Entity closest = this.entity.findClosestEntity(this.maxDistance);
        if (closest == null) return false;

        Vector dir = this.entity.getCurrentLocation().toVector()
                .subtract(closest.getLocation().toVector());

        double dx = dir.getX();
        double dy = dir.getY();
        double dz = dir.getZ();

        double distanceXZ = Math.sqrt(dx * dx + dz * dz);

        double pitchRad = Math.atan2(dy, distanceXZ);
        double yawRad = Math.atan2(dx, dz);

        float pitch = (float) Math.toDegrees(pitchRad);
        float yaw = (float) Math.toDegrees(yawRad);

        this.entity.rotate(180-yaw, pitch);

        return false;
    }

}
