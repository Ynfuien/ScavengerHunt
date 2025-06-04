package pl.ynfuien.scavengerHunt.core.tasks;

public abstract class Task {
    protected final Type type;
    protected boolean completed;

    protected Task(Type type) {
        this.type = type;
    }

    public Type getType() {
        return type;
    }

    public boolean isCompleted() {
        return completed;
    }

    public enum Type {
        ITEM,
        MOB
    }
}
