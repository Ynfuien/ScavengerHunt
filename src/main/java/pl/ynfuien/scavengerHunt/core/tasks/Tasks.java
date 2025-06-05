package pl.ynfuien.scavengerHunt.core.tasks;

import org.bukkit.configuration.ConfigurationSection;
import pl.ynfuien.scavengerHunt.ScavengerHunt;
import pl.ynfuien.scavengerHunt.core.tasks.biome.BiomeTasks;
import pl.ynfuien.scavengerHunt.core.tasks.item.ItemTasks;
import pl.ynfuien.scavengerHunt.core.tasks.mob.MobTasks;
import pl.ynfuien.scavengerHunt.core.tasks.ride.RideTasks;
import pl.ynfuien.scavengerHunt.core.tasks.trade.TradeTasks;
import pl.ynfuien.ydevlib.messages.YLogger;

import java.util.ArrayList;
import java.util.List;

public class Tasks {
    private final ScavengerHunt instance;

    private final ItemTasks itemTasks;
    private final MobTasks mobTasks;
    private final BiomeTasks biomeTasks;
    private final TradeTasks tradeTasks;
    private final RideTasks rideTasks;

    private final List<ITaskGenerator> taskGenerators = new ArrayList<>();

    public Tasks(ScavengerHunt instance) {
        this.instance = instance;
        this.itemTasks = new ItemTasks(instance);
        this.mobTasks = new MobTasks(instance);
        this.biomeTasks = new BiomeTasks(instance);
        this.tradeTasks = new TradeTasks(instance);
        this.rideTasks = new RideTasks(instance);
    }

    public boolean load(ConfigurationSection config) {
        if (config == null) return false;

        YLogger.info("Loading tasks...");

        if (!itemTasks.load(config.getConfigurationSection("get-item"))) {
            YLogger.error("Plugin couldn't load 'get-item' configuration!");
            return false;
        }

        if (!mobTasks.load(config.getConfigurationSection("kill-mob"))) {
            YLogger.error("Plugin couldn't load 'kill-mob' configuration!");
            return false;
        }

        if (!biomeTasks.load(config.getConfigurationSection("find-biome"))) {
            YLogger.error("Plugin couldn't load 'find-biome' configuration!");
            return false;
        }

        if (!tradeTasks.load(config.getConfigurationSection("trade-villager"))) {
            YLogger.error("Plugin couldn't load 'trade-villager' configuration!");
            return false;
        }

        if (!rideTasks.load(config.getConfigurationSection("ride-vehicle"))) {
            YLogger.error("Plugin couldn't load 'ride-vehicle' configuration!");
            return false;
        }

        taskGenerators.clear();
        if (itemTasks.isEnabled()) taskGenerators.add(itemTasks);
        if (mobTasks.isEnabled()) taskGenerators.add(mobTasks);
        if (biomeTasks.isEnabled()) taskGenerators.add(biomeTasks);
        if (tradeTasks.isEnabled()) taskGenerators.add(tradeTasks);
        if (rideTasks.isEnabled()) taskGenerators.add(rideTasks);

        if (taskGenerators.isEmpty()) {
            YLogger.error("At least one task type has to be enabled!");
            return false;
        }

        return true;
    }


    public Task<?> createTask() {
        int random = ScavengerHunt.randomBetween(0, taskGenerators.size());
        ITaskGenerator taskGenerator = taskGenerators.get(random);

        return taskGenerator.createTask();
    }

    public ItemTasks getItemTasks() {
        return itemTasks;
    }

    public MobTasks getMobTasks() {
        return mobTasks;
    }

    public BiomeTasks getBiomeTasks() {
        return biomeTasks;
    }
}
