package pl.ynfuien.scavengerHunt.core.tasks;

import org.bukkit.Material;

public class ItemTask extends Task {
    private final Material item;

    public ItemTask(Material item) {
        super(Type.ITEM);
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
