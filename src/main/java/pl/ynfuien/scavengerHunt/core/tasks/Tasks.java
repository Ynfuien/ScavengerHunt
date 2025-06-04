package pl.ynfuien.scavengerHunt.core.tasks;

import org.bukkit.configuration.ConfigurationSection;
import pl.ynfuien.scavengerHunt.ScavengerHunt;
import pl.ynfuien.ydevlib.messages.YLogger;

import java.util.ArrayList;
import java.util.List;

public class Tasks {
    private final ScavengerHunt instance;

    private final ItemTasks itemTasks;
    private final MobTasks mobTasks;

    private final List<ITaskGenerator> taskGenerators = new ArrayList<>();

    public Tasks(ScavengerHunt instance) {
        this.instance = instance;
        this.itemTasks = new ItemTasks(instance);
        this.mobTasks = new MobTasks(instance);
    }

    public boolean load(ConfigurationSection config) {
        if (config == null) return false;

        YLogger.info("Loading tasks...");

        if (!itemTasks.load(config.getConfigurationSection("find-item"))) {
            YLogger.error("Plugin couldn't load 'find-item' configuration!");
            return false;
        }

        if (!mobTasks.load(config.getConfigurationSection("kill-mob"))) {
            YLogger.error("Plugin couldn't load 'kill-mob' configuration!");
            return false;
        }

        taskGenerators.clear();
        if (itemTasks.isEnabled()) taskGenerators.add(itemTasks);
        if (mobTasks.isEnabled()) taskGenerators.add(mobTasks);

        if (taskGenerators.isEmpty()) {
            YLogger.error("At least one task type has to be enabled!");
            return false;
        }

        YLogger.info("Loaded tasks!");
        return true;
    }


    public Task createTask() {
        int random = ScavengerHunt.randomBetween(0, taskGenerators.size());
        ITaskGenerator taskGenerator = taskGenerators.get(random);

        return taskGenerator.createTask();
    }
}
