package pl.ynfuien.scavengerHunt.core.dto;

import org.bukkit.entity.Player;
import pl.ynfuien.scavengerHunt.ScavengerHunt;
import pl.ynfuien.scavengerHunt.core.hunts.Hunt;
import pl.ynfuien.scavengerHunt.core.tasks.Task;
import pl.ynfuien.ydevlib.messages.YLogger;

import java.util.ArrayList;
import java.util.List;

public class HuntDTO {
    private final long startTimestamp;
    private final List<TaskDTO> tasks = new ArrayList<>();

    public HuntDTO(Hunt hunt) {
        this.startTimestamp = hunt.getStartTimestamp();

        for (Task<?> task : hunt.getTasks()) {
            tasks.add(new TaskDTO(task));
        }
    }

    public Hunt fromDTO(ScavengerHunt instance, Player player) {
        List<Task<?>> tasks = new ArrayList<>();

        for (TaskDTO taskDTO : this.tasks) {
            Task<?> task = taskDTO.fromDTO();
            if (task == null) {
                YLogger.warn("Couldn't load a task from the player PDC!");
                continue;
            }

            tasks.add(task);
        }

        return new Hunt(instance, player, startTimestamp, tasks);
    }
}
