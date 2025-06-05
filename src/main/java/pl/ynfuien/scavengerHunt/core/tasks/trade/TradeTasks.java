package pl.ynfuien.scavengerHunt.core.tasks.trade;

import io.papermc.paper.registry.RegistryAccess;
import io.papermc.paper.registry.RegistryKey;
import org.bukkit.NamespacedKey;
import org.bukkit.Registry;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Villager;
import org.jetbrains.annotations.NotNull;
import pl.ynfuien.scavengerHunt.ScavengerHunt;
import pl.ynfuien.scavengerHunt.core.tasks.ITaskGenerator;
import pl.ynfuien.ydevlib.messages.YLogger;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class TradeTasks implements ITaskGenerator {
    private final ScavengerHunt instance;

    private boolean enabled = false;
    private final List<Villager.Profession> professions = new ArrayList<>();
    private final Set<Villager.Profession> blacklist = new HashSet<>();

    public static final Registry<Villager.@NotNull Profession> professionRegistry = RegistryAccess.registryAccess().getRegistry(RegistryKey.VILLAGER_PROFESSION);

    public TradeTasks(ScavengerHunt instance) {
        this.instance = instance;
        for (Villager.Profession profession : professionRegistry) professions.add(profession);
    }

    public boolean load(ConfigurationSection config) {
        if (config == null) return false;

        enabled = config.getBoolean("enabled");
        if (!enabled) return true;

        YLogger.info("Loading trade-villager...");
        blacklist.clear();

        List<String> list = config.getStringList("blacklist");
        for (String value : list) {
            NamespacedKey key = NamespacedKey.minecraft(value.toLowerCase());
            Villager.Profession profession = professionRegistry.get(key);
            if (profession == null) {
                YLogger.warn(String.format("Blacklist profession '%s' for the trade tasks is incorrect!", value));
                continue;
            }

            blacklist.add(profession);
        }

        return true;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public List<Villager.Profession> getProfessions() {
        return professions;
    }

    public Set<Villager.Profession> getBlacklist() {
        return blacklist;
    }

    @Override
    public TradeTask createTask() {
        Villager.Profession profession;
        do {
            int randomIndex = ScavengerHunt.randomBetween(0, professions.size());
            profession = professions.get(randomIndex);
        } while (blacklist.contains(profession));

        return new TradeTask(profession);
    }
}
