package pl.ynfuien.scavengerHunt.core.dto;

import org.bukkit.Keyed;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.EntityType;
import pl.ynfuien.scavengerHunt.core.tasks.Task;
import pl.ynfuien.scavengerHunt.core.tasks.biome.BiomeTask;
import pl.ynfuien.scavengerHunt.core.tasks.biome.BiomeTasks;
import pl.ynfuien.scavengerHunt.core.tasks.item.ItemTask;
import pl.ynfuien.scavengerHunt.core.tasks.mob.MobTask;
import pl.ynfuien.scavengerHunt.core.tasks.ride.RideTask;
import pl.ynfuien.scavengerHunt.core.tasks.trade.TradeTask;
import pl.ynfuien.scavengerHunt.core.tasks.trade.TradeTasks;

public class TaskDTO {
    private final String type;
    private final String goal;
    private final boolean completed;

    public TaskDTO(Task<?> task) {
        this.type = task.getClass().getSimpleName();

        Object goal = task.getGoal();
        if (goal instanceof Keyed keyed) {
            this.goal = keyed.key().value();
        } else {
            this.goal = goal.toString();
        }

        this.completed = task.isCompleted();
    }


    public Task<?> fromDTO() {
        Task<?> task = null;
        switch (type) {
            case "BiomeTask" -> task = new BiomeTask(BiomeTasks.biomeRegistry.get(NamespacedKey.minecraft(goal)));
            case "MobTask" -> task = new MobTask(EntityType.valueOf(goal));
            case "ItemTask" -> task = new ItemTask(Material.matchMaterial(goal));
            case "TradeTask" -> task = new TradeTask(TradeTasks.professionRegistry.get(NamespacedKey.minecraft(goal)));
            case "RideTask" -> task = new RideTask(EntityType.valueOf(goal));
        }

        if (task == null) return null;
        task.setCompleted(completed);

        return task;
    }
}
