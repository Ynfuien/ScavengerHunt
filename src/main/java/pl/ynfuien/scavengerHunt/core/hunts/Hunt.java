package pl.ynfuien.scavengerHunt.core.hunts;

import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import pl.ynfuien.scavengerHunt.Lang;
import pl.ynfuien.scavengerHunt.ScavengerHunt;
import pl.ynfuien.scavengerHunt.core.tasks.item.ItemTask;
import pl.ynfuien.scavengerHunt.core.tasks.mob.MobTask;
import pl.ynfuien.scavengerHunt.core.tasks.Task;
import pl.ynfuien.scavengerHunt.core.tasks.Tasks;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Hunt {
    private final ScavengerHunt instance;
    private final Player player;
    private final long startTimestamp;
    private final List<Task> tasks = new ArrayList<>();

    public Hunt(ScavengerHunt instance, Player player, int taskAmount) {
        this.instance = instance;
        this.player = player;
        this.startTimestamp = System.currentTimeMillis();


        Tasks tasks = instance.getTasks();
        for (int i = 0; i < taskAmount; i++) {
            Task task;
            do {
                task = tasks.createTask();
            } while (this.tasks.contains(task));

            this.tasks.add(task);
        }
    }

    public Player getPlayer() {
        return player;
    }

    public long getStartTimestamp() {
        return startTimestamp;
    }

    public List<Task> getTasks() {
        return tasks;
    }

    public int getCompletedTaskCount() {
        return (int) tasks.stream().filter(Task::isCompleted).count();
    }

    public void itemPickedUp(Material item) {
        for (Task task : tasks) {
            if (!(task instanceof ItemTask itemTask)) continue;
            if (!itemTask.getItem().equals(item)) continue;

            task.setCompleted(true);

            HashMap<String, Object> placeholders = new HashMap<>();
            placeholders.put("tasks-left", tasks.size() - getCompletedTaskCount());
            Lang.Message.TASK_COMPLETED.send(player, placeholders);
            break;
        }
    }

    public void mobKilled(EntityType mob) {
        for (Task task : tasks) {
            if (!(task instanceof MobTask mobTask)) continue;
            if (!mobTask.getMob().equals(mob)) continue;

            task.setCompleted(true);

            HashMap<String, Object> placeholders = new HashMap<>();
            placeholders.put("tasks-left", tasks.size() - getCompletedTaskCount());
            Lang.Message.TASK_COMPLETED.send(player, placeholders);
            break;
        }
    }
}
