package pl.ynfuien.scavengerHunt.core.tasks.item;

import org.bukkit.Material;
import pl.ynfuien.scavengerHunt.core.tasks.Task;

public class ItemTask extends Task<Material> {
    public ItemTask(Material item) {
        super(item);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (obj instanceof ItemTask task) return task.getGoal().equals(this.goal);

        return super.equals(obj);
    }
}
