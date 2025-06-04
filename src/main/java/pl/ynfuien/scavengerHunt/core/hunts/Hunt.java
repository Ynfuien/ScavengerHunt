package pl.ynfuien.scavengerHunt.core.hunts;

import org.bukkit.entity.Player;
import pl.ynfuien.scavengerHunt.ScavengerHunt;
import pl.ynfuien.scavengerHunt.core.tasks.Task;
import pl.ynfuien.scavengerHunt.core.tasks.Tasks;

import java.util.ArrayList;
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
}
