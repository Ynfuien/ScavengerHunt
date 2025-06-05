package pl.ynfuien.scavengerHunt.core.tasks.biome;

import org.bukkit.block.Biome;
import pl.ynfuien.scavengerHunt.core.tasks.Task;

public class BiomeTask extends Task<Biome> {
    public BiomeTask(Biome biome) {
        super(biome);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (obj instanceof BiomeTask task) return task.getGoal().equals(this.goal);

        return super.equals(obj);
    }
}
