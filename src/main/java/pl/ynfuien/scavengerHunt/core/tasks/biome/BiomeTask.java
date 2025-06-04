package pl.ynfuien.scavengerHunt.core.tasks.biome;

import org.bukkit.block.Biome;
import org.bukkit.entity.EntityType;
import pl.ynfuien.scavengerHunt.core.tasks.Task;

public class BiomeTask extends Task {
    private final Biome biome;

    public BiomeTask(Biome biome) {
        this.biome = biome;
    }

    public Biome getBiome() {
        return biome;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (obj instanceof BiomeTask task) return task.getBiome().equals(this.biome);

        return super.equals(obj);
    }
}
