package pl.ynfuien.scavengerHunt.core.tasks.item;

import org.bukkit.Material;
import pl.ynfuien.scavengerHunt.core.tasks.Task;

public class ItemTask extends Task {
    private final Material item;

    public ItemTask(Material item) {
        this.item = item;
    }

    public Material getItem() {
        return item;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (obj instanceof ItemTask task) return task.getItem().equals(this.item);

        return super.equals(obj);
    }
}
