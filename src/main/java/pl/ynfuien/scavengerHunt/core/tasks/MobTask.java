package pl.ynfuien.scavengerHunt.core.tasks;

import org.bukkit.entity.EntityType;

public class MobTask extends Task {
    private final EntityType mob;

    public MobTask(EntityType mob) {
        super(Type.ITEM);
        this.mob = mob;
    }

    public EntityType getMob() {
        return mob;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (obj instanceof MobTask task) return task.getMob().equals(this.mob);

        return super.equals(obj);
    }
}
