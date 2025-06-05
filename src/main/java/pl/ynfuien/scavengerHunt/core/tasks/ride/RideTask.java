package pl.ynfuien.scavengerHunt.core.tasks.ride;

import org.bukkit.entity.EntityType;
import pl.ynfuien.scavengerHunt.core.tasks.Task;

public class RideTask extends Task<EntityType> {
    public RideTask(EntityType vehicle) {
        super(vehicle);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (obj instanceof RideTask task) return task.getGoal().equals(this.goal);

        return super.equals(obj);
    }
}
