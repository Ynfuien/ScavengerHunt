package pl.ynfuien.scavengerHunt.core.tasks;

import pl.ynfuien.scavengerHunt.core.dto.TaskDTO;

public abstract class Task<T> {
    protected T goal;
    protected boolean completed;

    protected Task(T goal) {
        this.goal = goal;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    public T getGoal() {
        return goal;
    }

    public boolean isGoalEqual(Object goal) {
        return this.goal.equals(goal);
    }
}
