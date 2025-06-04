package pl.ynfuien.scavengerHunt.core.hunts;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import pl.ynfuien.scavengerHunt.ScavengerHunt;
import pl.ynfuien.ydevlib.messages.YLogger;

import java.util.HashMap;

public class Hunts {
    private final ScavengerHunt instance;

    private boolean autoAssign = true;
    private int minTaskAmount = 5;
    private int maxTaskAmount = 8;

    private int timeLimit = 48;

    private final Rewards rewards;

    private final HashMap<Player, Hunt> hunts = new HashMap<>();

    public Hunts(ScavengerHunt instance) {
        this.instance = instance;
        this.rewards = new Rewards(instance);
    }

    public boolean load(ConfigurationSection config) {
        if (config == null) return false;

        YLogger.info("Loading hunts...");


        autoAssign = config.getBoolean("auto-assign");

        minTaskAmount = config.getInt("task-amount.min");
        if (minTaskAmount < 1) {
            YLogger.warn("task-amount.min can't be lower than 1! Value of 1 will be used.");
            minTaskAmount = 1;
        }

        maxTaskAmount = config.getInt("task-amount.max");
        if (maxTaskAmount < 1) {
            YLogger.warn("task-amount.max can't be lower than 1! Value of 1 will be used.");
            maxTaskAmount = 1;
        }

        if (minTaskAmount > maxTaskAmount) {
            YLogger.warn("task-amount.min can't be higher than task-amount.max! Value of task-amount.max will be used for both settings.");
            maxTaskAmount = minTaskAmount;
        }

        timeLimit = config.getInt("time-limit");
        if (timeLimit < 1) {
            YLogger.warn("time-limit can't be lower than 1! Value of 1 will be used.");
            timeLimit = 1;
        }

        rewards.load(config.getConfigurationSection("rewards"));


        YLogger.info("Loaded hunts!");
        return true;
    }

    private Hunt createHunt(Player player) {
        int tasks = ScavengerHunt.randomBetween(minTaskAmount, maxTaskAmount + 1);

        return new Hunt(instance, player, tasks);
    }

    public Hunt getHunt(Player player) {
        return getHunt(player, false);
    }

    public Hunt getHunt(Player player, boolean force) {
        Hunt hunt = getSavedHunt(player);
        if (hunt != null) {
            hunts.put(player, hunt);
            return hunt;
        }

        if (!force && !autoAssign) return null;
        hunt = createHunt(player);
        hunts.put(player, hunt);

        return hunt;
    }

    private Hunt getSavedHunt(Player player) {
        return null;
    }

    public boolean isAutoAssign() {
        return autoAssign;
    }

    public int getMinTaskAmount() {
        return minTaskAmount;
    }

    public int getMaxTaskAmount() {
        return maxTaskAmount;
    }

    public int getTimeLimit() {
        return timeLimit;
    }

    public Rewards getRewards() {
        return rewards;
    }
}
