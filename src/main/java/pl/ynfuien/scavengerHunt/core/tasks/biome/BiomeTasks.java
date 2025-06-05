package pl.ynfuien.scavengerHunt.core.tasks.biome;

import io.papermc.paper.registry.RegistryAccess;
import io.papermc.paper.registry.RegistryKey;
import org.bukkit.NamespacedKey;
import org.bukkit.Registry;
import org.bukkit.block.Biome;
import org.bukkit.configuration.ConfigurationSection;
import org.jetbrains.annotations.NotNull;
import pl.ynfuien.scavengerHunt.ScavengerHunt;
import pl.ynfuien.scavengerHunt.core.tasks.ITaskGenerator;
import pl.ynfuien.ydevlib.messages.YLogger;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class BiomeTasks implements ITaskGenerator {
    private final ScavengerHunt instance;

    private boolean enabled = false;
    private final List<Biome> biomes = new ArrayList<>();
    private final Set<Biome> blacklist = new HashSet<>();

    private final Registry<@NotNull Biome> biomeRegistry = RegistryAccess.registryAccess().getRegistry(RegistryKey.BIOME);

    public BiomeTasks(ScavengerHunt instance) {
        this.instance = instance;

        for (Biome biome : biomeRegistry) biomes.add(biome);
    }

    public boolean load(ConfigurationSection config) {
        if (config == null) return false;

        enabled = config.getBoolean("enabled");
        if (!enabled) return true;

        YLogger.info("Loading find-biome...");
        blacklist.clear();

        List<String> list = config.getStringList("blacklist");
        for (String value : list) {
            NamespacedKey key = NamespacedKey.minecraft(value.toLowerCase());
            Biome biome = biomeRegistry.get(key);
            if (biome == null) {
                YLogger.warn(String.format("Blacklist biome '%s' for the biome tasks is incorrect!", value));
                continue;
            }

            blacklist.add(biome);
        }

        YLogger.info("Loaded find-biome!");
        return true;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public List<Biome> getBiomes() {
        return biomes;
    }

    public Set<Biome> getBlacklist() {
        return blacklist;
    }

    @Override
    public BiomeTask createTask() {
        Biome mob;
        do {
            int randomIndex = ScavengerHunt.randomBetween(0, biomes.size());
            mob = biomes.get(randomIndex);
        } while (blacklist.contains(mob));

        return new BiomeTask(mob);
    }
}
