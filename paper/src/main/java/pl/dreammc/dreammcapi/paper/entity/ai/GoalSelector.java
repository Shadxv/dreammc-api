package pl.dreammc.dreammcapi.paper.entity.ai;

import java.util.LinkedList;

public class GoalSelector {

    private final LinkedList<Goal> goals;

    public GoalSelector() {
        this.goals = new LinkedList<>();
    }

    public void addGoal(Goal goalComponent) {
        this.goals.add(goalComponent);
    }

    public void tick(double deltaTime) {
        for (Goal goal : this.goals) {
            if (goal.tick(deltaTime)) break;
        }
    }

    @SuppressWarnings("unchecked")
    public <T extends Goal> T getGoalComponent(Class<T> clazz) {
        for (Goal goal : this.goals) {
            if (clazz.isInstance(goal)) {
                return (T) goal;
            }
        }
        return null;
    }

}
