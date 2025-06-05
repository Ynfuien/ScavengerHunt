package pl.ynfuien.scavengerHunt.core.hunts;

import com.google.gson.Gson;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import pl.ynfuien.scavengerHunt.Lang;
import pl.ynfuien.scavengerHunt.ScavengerHunt;
import pl.ynfuien.scavengerHunt.core.dto.HuntDTO;
import pl.ynfuien.ydevlib.messages.YLogger;

import java.util.HashMap;

public class Hunts {
    private final ScavengerHunt instance;

    private final NamespacedKey huntNamespacedKey;
    private final static Gson GSON = new Gson();

    private boolean autoAssign = true;
    private int minTaskAmount = 5;
    private int maxTaskAmount = 8;

    private int timeLimit = 48;

    private final Rewards rewards;

    private final HashMap<Player, Hunt> hunts = new HashMap<>();

    public Hunts(ScavengerHunt instance) {
        this.instance = instance;
        this.huntNamespacedKey = new NamespacedKey(instance, "active-hunt");
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

    public Hunt createNewHunt(Player player) {
        int tasks = ScavengerHunt.randomBetween(minTaskAmount, maxTaskAmount + 1);

        Hunt hunt = new Hunt(instance, player, tasks);
        hunts.put(player, hunt);
        saveHunt(player);

        Lang.Message.HUNT_ASSIGNED.send(player);
        return hunt;
    }

    public Hunt getCurrentHunt(Player player) {
        Hunt hunt = getSavedHunt(player);
        if (hunt != null) hunts.put(player, hunt);

        return hunt;
    }

    public void autoAssignNewHunt(Player player) {
        if (!autoAssign) return;
        createNewHunt(player);
    }

    private Hunt getSavedHunt(Player player) {
        if (hunts.containsKey(player)) return hunts.get(player);

        PersistentDataContainer pdc = player.getPersistentDataContainer();

        String json = pdc.get(huntNamespacedKey, PersistentDataType.STRING);
        if (json == null) return null;

        HuntDTO dto = GSON.fromJson(json, HuntDTO.class);
        return dto.fromDTO(instance, player);
    }

    public void saveHunt(Player player) {
        PersistentDataContainer pdc = player.getPersistentDataContainer();

        Hunt hunt = hunts.get(player);
        if (hunt == null) {
            pdc.remove(huntNamespacedKey);
            return;
        }

        HuntDTO dto = new HuntDTO(hunt);
        String json = GSON.toJson(dto);
        pdc.set(huntNamespacedKey, PersistentDataType.STRING, json);
    }

    public void abortHunt(Player player) {
        hunts.remove(player);
        saveHunt(player);
    }

    public void removeHuntFromCache(Player player) {
        saveHunt(player);
        hunts.remove(player);
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
