package pl.ynfuien.scavengerHunt.core.tasks.mob;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.EntityType;
import pl.ynfuien.scavengerHunt.ScavengerHunt;
import pl.ynfuien.scavengerHunt.core.tasks.ITaskGenerator;
import pl.ynfuien.ydevlib.messages.YLogger;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class    MobTasks implements ITaskGenerator {
    private final ScavengerHunt instance;

    private boolean enabled = false;
    private final List<EntityType> mobs;
    private final Set<EntityType> blacklist = new HashSet<>();

    public MobTasks(ScavengerHunt instance) {
        this.instance = instance;
        this.mobs = getKillableMobs();
    }

    public boolean load(ConfigurationSection config) {
        if (config == null) return false;

        enabled = config.getBoolean("enabled");
        if (!enabled) return true;

        YLogger.info("Loading kill-mob...");
        blacklist.clear();

        List<String> list = config.getStringList("blacklist");
        for (String value : list) {
            EntityType mob = EntityType.fromName(value);
            if (mob == null) {
                YLogger.warn(String.format("Blacklist mob '%s' for the mob tasks is incorrect!", value));
                continue;
            }

            blacklist.add(mob);
        }

        YLogger.info("Loaded kill-mob!");
        return true;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public List<EntityType> getMobs() {
        return mobs;
    }

    public Set<EntityType> getBlacklist() {
        return blacklist;
    }

    @Override
    public MobTask createTask() {
        EntityType mob;
        do {
            int randomIndex = ScavengerHunt.randomBetween(0, mobs.size());
            mob = mobs.get(randomIndex);
        } while (blacklist.contains(mob));

        return new MobTask(mob);
    }

    private static List<EntityType> getKillableMobs() {
        List<EntityType> mobs = new ArrayList<>();

        for (EntityType mob : EntityType.values()) {
            if (!mob.isAlive()) continue;
            mobs.add(mob);
        }

        return mobs;
    }
}
