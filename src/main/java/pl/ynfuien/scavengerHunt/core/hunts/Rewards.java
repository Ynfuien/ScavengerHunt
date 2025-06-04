package pl.ynfuien.scavengerHunt.core.hunts;

import org.apache.commons.lang3.tuple.Pair;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import pl.ynfuien.scavengerHunt.ScavengerHunt;
import pl.ynfuien.ydevlib.messages.YLogger;

import java.util.HashMap;
import java.util.Set;

public class Rewards {
    private final ScavengerHunt instance;

    private boolean experienceEnabled = true;
    private int minExperienceAmount = 0;
    private int maxExperienceAmount = 3;

    private boolean itemsEnabled = true;
    private final HashMap<Material, Pair<Integer, Integer>> itemList = new HashMap<>();

    private boolean moneyEnabled = true;
    private int minMoneyAmount = 0;
    private int maxMoneyAmount = 1000;

    public Rewards(ScavengerHunt instance) {
        this.instance = instance;
    }

    public boolean load(ConfigurationSection config) {
        if (config == null) return false;

        YLogger.info("Loading hunt rewards...");

        experienceEnabled = config.getBoolean("experience.enabled");
        if (experienceEnabled) {
            minExperienceAmount = config.getInt("experience.min");
            if (minExperienceAmount < 0) {
                YLogger.warn("experience.min can't be lower than 0! Value of 0 will be used.");
                minExperienceAmount = 0;
            }

            maxExperienceAmount = config.getInt("experience.max");
            if (maxExperienceAmount < 0) {
                YLogger.warn("experience.max can't be lower than 0! Value of 0 will be used.");
                maxExperienceAmount = 0;
            }

            if (minExperienceAmount > maxExperienceAmount) {
                YLogger.warn("experience.min can't be higher than experience.max! Value of experience.max will be used for both settings.");
                maxExperienceAmount = minExperienceAmount;
            }
        }

        itemsEnabled = config.getBoolean("items.enabled");
        if (itemsEnabled) {
            ConfigurationSection itemsSection = config.getConfigurationSection("items.list");
            Set<String> keys = itemsSection.getKeys(false);

            for (String key : keys) {
                Material item = Material.matchMaterial(key);
                if (item == null) {
                    YLogger.warn(String.format("Item award '%s' is incorrect, it won't be used!", key));
                    continue;
                }

                if (!itemsSection.isInt(key + ".min")) {
                    YLogger.warn(String.format("Item '%s' doesn't have correct 'min' value!"));
                    continue;
                }

                if (!itemsSection.isInt(key + ".max")) {
                    YLogger.warn(String.format("Item '%s' doesn't have correct 'max' value!"));
                    continue;
                }

                Pair<Integer, Integer> minMaxPair = Pair.of(itemsSection.getInt(key + ".min"), itemsSection.getInt(key + ".max"));
                itemList.put(item, minMaxPair);
            }
        }

        moneyEnabled = config.getBoolean("money.enabled");
        if (moneyEnabled) {
            minMoneyAmount = config.getInt("money.min");
            if (minMoneyAmount < 0) {
                YLogger.warn("money.min can't be lower than 0! Value of 0 will be used.");
                minMoneyAmount = 0;
            }

            maxMoneyAmount = config.getInt("money.max");
            if (maxMoneyAmount < 0) {
                YLogger.warn("money.max can't be lower than 0! Value of 0 will be used.");
                maxMoneyAmount = 0;
            }

            if (minMoneyAmount > maxMoneyAmount) {
                YLogger.warn("money.min can't be higher than money.max! Value of money.max will be used for both settings.");
                maxMoneyAmount = minMoneyAmount;
            }
        }

        YLogger.info("Loaded hunt rewards!");
        return true;
    }

    public void rewardPlayer(Player player) {
        if (experienceEnabled) {
            int value = ScavengerHunt.randomBetween(minExperienceAmount, maxExperienceAmount + 1);
            player.giveExpLevels(value);
        }

        if (itemsEnabled) {
            int random = ScavengerHunt.randomBetween(0, itemList.size());

            int i = 0;
            for (Material item : itemList.keySet()) {
                if (i == random) {
                    Pair<Integer, Integer> minMaxPair = itemList.get(item);

                    int value = ScavengerHunt.randomBetween(minMaxPair.getKey(), minMaxPair.getValue() + 1);
                    player.give(new ItemStack(item, value));
                    break;
                }

                i++;
            }
        }

//        if (moneyEnabled) {
//
//        }
    }
}
