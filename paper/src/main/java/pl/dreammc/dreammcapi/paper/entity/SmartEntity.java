package pl.dreammc.dreammcapi.paper.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pl.dreammc.dreammcapi.paper.entity.ai.Goal;
import pl.dreammc.dreammcapi.paper.entity.ai.GoalSelector;

public abstract class SmartEntity<T extends SmartEntity> {

    @Getter @Setter private double moveSpeed;
    @Getter @Setter private float rotateSpeed;
    @Getter private final GoalSelector goalSelector;

    protected SmartEntity() {
        this.moveSpeed = 1;
        this.rotateSpeed = 45;
        this.goalSelector = new GoalSelector();
        SmartEntityManager.getInstance().registerEntity(this);
    }

    protected SmartEntity(double moveSpeed, float rotateSpeed) {
        this.moveSpeed = moveSpeed;
        this.rotateSpeed = rotateSpeed;
        this.goalSelector = new GoalSelector();
        SmartEntityManager.getInstance().registerEntity(this);
    }

    public T addGoalComponent(@NotNull Goal goalComponent) {
        goalComponent.setEntity(this);
        this.goalSelector.addGoal(goalComponent);
        return (T) this;
    }

    public abstract void tick(double deltaTime);

    public abstract void move(double deltaX, double deltaY, double deltaZ);
    public abstract void move(double deltaX, double deltaY, double deltaZ, float yaw, float pitch);
    public abstract void rotate(float yaw, float pitch);
    public abstract void playAnimation();
    public abstract void attack();
    public abstract @Nullable Entity findClosestEntity(double radius);

    public abstract Location getCurrentLocation();

}
