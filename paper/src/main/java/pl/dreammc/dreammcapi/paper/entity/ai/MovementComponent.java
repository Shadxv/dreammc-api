package pl.dreammc.dreammcapi.paper.entity.ai;

import org.bukkit.Location;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.Nullable;

public class MovementComponent extends Goal {

    @Nullable private Location targetLocation;
    @Nullable private Vector movementVector;

    @Override
    public boolean tick(double deltaTime) {
        if (targetLocation == null) return false;
        if (movementVector == null) {
            this.targetLocation = null;
            return false;
        }

        double deltaTimeSeconds = deltaTime/1000.0d;

        Vector currentTickMoveVector = this.movementVector.clone().multiply(this.entity.getMoveSpeed());
        currentTickMoveVector.multiply(deltaTimeSeconds);

        double lengthDifference = this.entity.getCurrentLocation().distance(this.targetLocation) - currentTickMoveVector.length();

        double dx;
        double dy;
        double dz;

        if (lengthDifference <= 0) {
            dx = this.targetLocation.getX() - this.entity.getCurrentLocation().getX();
            dy = this.targetLocation.getY() - this.entity.getCurrentLocation().getY();
            dz = this.targetLocation.getZ() - this.entity.getCurrentLocation().getZ();
            this.targetLocation = null;
            this.movementVector = null;
            // Add yaw and pitch
        } else {
            dx = currentTickMoveVector.getX();
            dy = currentTickMoveVector.getY();
            dz = currentTickMoveVector.getZ();
            // Add yaw and pitch
        }

        double yawRadians = (dx != 0)
                ? ((dx < 0 ? 1.5 * Math.PI : 0.5 * Math.PI) - Math.atan(dz / dx))
                : (dz < 0 ? Math.PI : 0.0);
        float yaw = (float) (-yawRadians * 180 / Math.PI - 90);
        float dYaw = yaw - this.entity.getCurrentLocation().getYaw();
        dYaw = Math.max(-this.entity.getRotateSpeed(), Math.min(this.entity.getRotateSpeed(), dYaw));
        dYaw *= (float) deltaTimeSeconds;

        double distanceXZ = Math.sqrt(dx*dx + dz*dz);
        double pitchRadians = Math.asin(dy / new Vector(dx, dy, dz).length());
        float pitch = (float)Math.toDegrees(pitchRadians);
        float dPitch = pitch - this.entity.getCurrentLocation().getPitch();
        dPitch = Math.max(-this.entity.getRotateSpeed(), Math.min(this.entity.getRotateSpeed(), dYaw));
        dPitch *= (float) deltaTimeSeconds;

        float finalYaw = this.entity.getCurrentLocation().getYaw() + dYaw;
        float finalPitch = this.entity.getCurrentLocation().getPitch() + dPitch;

        this.entity.move(dx, dy, dz, finalYaw, finalPitch);
        return false;
    }

    public void setTargetLocation(@Nullable Location targetLocation) {
        if (!targetLocation.getWorld().equals(this.entity.getCurrentLocation().getWorld())) return;
        this.targetLocation = targetLocation;
        this.movementVector = targetLocation.clone().subtract(this.entity.getCurrentLocation()).toVector().normalize();
    }

}
