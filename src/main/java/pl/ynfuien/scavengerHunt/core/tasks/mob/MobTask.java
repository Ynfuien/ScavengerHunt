package pl.ynfuien.scavengerHunt.core.tasks.mob;

import org.bukkit.entity.EntityType;
import pl.ynfuien.scavengerHunt.core.tasks.Task;

public class MobTask extends Task<EntityType> {
    public MobTask(EntityType mob) {
        super(mob);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (obj instanceof MobTask task) return task.getGoal().equals(this.goal);

        return super.equals(obj);
    }
}
