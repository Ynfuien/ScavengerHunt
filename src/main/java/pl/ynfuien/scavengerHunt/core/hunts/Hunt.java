package pl.ynfuien.scavengerHunt.core.hunts;

import io.papermc.paper.threadedregions.scheduler.ScheduledTask;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Biome;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import pl.ynfuien.scavengerHunt.Lang;
import pl.ynfuien.scavengerHunt.ScavengerHunt;
import pl.ynfuien.scavengerHunt.core.tasks.Task;
import pl.ynfuien.scavengerHunt.core.tasks.Tasks;
import pl.ynfuien.scavengerHunt.core.tasks.biome.BiomeTask;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Hunt {
    private final ScavengerHunt instance;
    private final Hunts hunts;
    private final Player player;
    private final long startTimestamp;
    private final List<Task<?>> tasks = new ArrayList<>();

    private ScheduledTask huntCheckInterval = null;
    private Biome lastBiome = null;

    public Hunt(ScavengerHunt instance, Player player, long startTimestamp, List<Task<?>> tasks) {
        this.instance = instance;
        this.hunts = instance.getHunts();
        this.player = player;
        this.startTimestamp = startTimestamp;
        this.tasks.addAll(tasks);

        for (Task<?> task : tasks) {
            if (task.isCompleted()) continue;
            if (!(task instanceof BiomeTask)) continue;

            startInterval(true);
            return;
        }

        startInterval(false);
    }

    public Hunt(ScavengerHunt instance, Player player, int taskAmount) {
        this.instance = instance;
        this.hunts = instance.getHunts();
        this.player = player;
        this.startTimestamp = System.currentTimeMillis();

        boolean isAnyBiomeTask = false;

        Tasks tasks = instance.getTasks();
        for (int i = 0; i < taskAmount; i++) {
            Task<?> task;
            do {
                task = tasks.createTask();
            } while (this.tasks.contains(task));

            if (task instanceof BiomeTask) isAnyBiomeTask = true;
            this.tasks.add(task);
        }

        startInterval(isAnyBiomeTask);
    }

    private void startInterval(boolean checkBiome) {
        huntCheckInterval = Bukkit.getGlobalRegionScheduler().runAtFixedRate(instance, (task) -> {
            if (getTimeLeft() <= 0) {
                Lang.Message.HUNT_ENDED.send(player);
                finishTheHunt(true);
                return;
            }

            if (checkBiome) {
                Location loc = player.getLocation();
                Biome current = loc.getWorld().getBiome(loc);
                if (current == lastBiome) return;

                lastBiome = current;
                handleTask(BiomeTask.class, current);
            }
        }, 20 * 2, 20 * 2);
    }

    public void finishTheHunt(boolean autoAssign) {
        hunts.abortHunt(player);
        huntCheckInterval.cancel();

        if (autoAssign) {
            Bukkit.getGlobalRegionScheduler().runDelayed(instance, (task) -> {
                hunts.autoAssignNewHunt(player);
            }, 20 * 5);
        }
    }

    public Player getPlayer() {
        return player;
    }

    public long getStartTimestamp() {
        return startTimestamp;
    }

    public List<Task<?>> getTasks() {
        return tasks;
    }

    public int getCompletedTaskCount() {
        return (int) tasks.stream().filter(Task::isCompleted).count();
    }

    public boolean isHuntCompleted() {
        return tasks.size() == getCompletedTaskCount();
    }

    /**
     * @return Time left in minutes
     */
    public int getTimeLeft() {
        long now = System.currentTimeMillis();
        long limit = (long) hunts.getTimeLimit() * 60 * 60 * 1000;

        return ((int) (startTimestamp + limit - now)) / 1000 / 60;
    }

    private void handleHuntCompletion() {
        HashMap<String, Object> placeholders = new HashMap<>();
        Rewards.Reward reward = hunts.getRewards().rewardPlayer(player);

        Lang.Message.HUNT_COMPLETED_HEADER.send(player);

        if (reward.experience() > 0) {
            placeholders.put("experience-level", reward.experience());
            Lang.Message.HUNT_COMPLETED_REWARD_EXPERIENCE.send(player, placeholders);
        }

        ItemStack item = reward.item();
        if (item != null) {
            String displayName = LegacyComponentSerializer.legacyAmpersand().serialize((item.effectiveName().color(null)));
            placeholders.put("item-amount", item.getAmount());
            placeholders.put("item-display-name", displayName);
            placeholders.put("item-display-name-lower-case", displayName.toLowerCase());
            placeholders.put("item-display-name-upper-case", displayName.toUpperCase());
            Lang.Message.HUNT_COMPLETED_REWARD_ITEM.send(player, placeholders);
        }

        if (reward.money() > 0) {
            placeholders.put("money-amount", reward.money());
            Lang.Message.HUNT_COMPLETED_REWARD_MONEY.send(player, placeholders);
        }

        finishTheHunt(true);
    }

    public boolean handleTask(Class<? extends Task<?>> taskClass, Object goal) {
        for (Task<?> task : tasks) {
            if (task.isCompleted()) continue;
            if (!(task.getClass().equals(taskClass))) continue;
            if (!task.isGoalEqual(goal)) continue;

            task.setCompleted(true);

            if (isHuntCompleted()) {
                handleHuntCompletion();
                return true;
            }

            HashMap<String, Object> placeholders = new HashMap<>();
            placeholders.put("tasks-left", tasks.size() - getCompletedTaskCount());
            Lang.Message.TASK_COMPLETED.send(player, placeholders);

            hunts.saveHunt(player);
            return true;
        }

        return false;
    }
}
