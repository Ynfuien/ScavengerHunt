package pl.ynfuien.scavengerHunt.core.tasks;

public abstract class Task {
    protected boolean completed;

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }
}
