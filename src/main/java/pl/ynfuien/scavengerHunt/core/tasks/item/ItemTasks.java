package pl.ynfuien.scavengerHunt.core.tasks.item;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.*;
import pl.ynfuien.scavengerHunt.ScavengerHunt;
import pl.ynfuien.scavengerHunt.core.tasks.ITaskGenerator;
import pl.ynfuien.ydevlib.messages.YLogger;

import java.util.*;

public class ItemTasks implements ITaskGenerator {
    private final ScavengerHunt instance;

    private boolean enabled = false;
    private final List<Material> items;
    private final Set<Material> blacklist = new HashSet<>();

    public ItemTasks(ScavengerHunt instance) {
        this.instance = instance;
        this.items = getObtainableItems();
    }

    public boolean load(ConfigurationSection config) {
        if (config == null) return false;

        enabled = config.getBoolean("enabled");
        if (enabled) return true;

        YLogger.info("Loading find-item...");
        items.clear();
        blacklist.clear();

        List<String> list = config.getStringList("blacklist");
        for (String value : list) {
            Material item = Material.matchMaterial(value);
            if (item == null) {
                YLogger.warn(String.format("Blacklist item '%s' for the item tasks is incorrect!", value));
                continue;
            }

            blacklist.add(item);
        }

        YLogger.info("Loaded find-item!");
        return true;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public List<Material> getItems() {
        return items;
    }

    public Set<Material> getBlacklist() {
        return blacklist;
    }

    @Override
    public ItemTask createTask() {
        Material item;
        do {
            int randomIndex = ScavengerHunt.randomBetween(0, items.size());
            item = items.get(randomIndex);
        } while (blacklist.contains(item));

        return new ItemTask(item);
    }

    private static List<Material> getObtainableItems() {
        Set<Material> items = new HashSet<>();

        Iterator<Recipe> iterator = Bukkit.recipeIterator();
        while (iterator.hasNext()) {
            Recipe recipe = iterator.next();

            if (recipe instanceof CraftingRecipe craftingRecipe) {
                if (!craftingRecipe.getKey().namespace().equalsIgnoreCase("minecraft")) continue;

                items.add(craftingRecipe.getResult().getType());

                if (craftingRecipe instanceof ShapedRecipe shapedRecipe) {
                    for (RecipeChoice recipeChoice : shapedRecipe.getChoiceMap().values()) {
                        if (recipeChoice instanceof RecipeChoice.ExactChoice choice) {
                            for (ItemStack item : choice.getChoices()) items.add(item.getType());
                            continue;
                        }

                        if (recipeChoice instanceof RecipeChoice.MaterialChoice choice) {
                            items.addAll(choice.getChoices());
                        }
                    }

                    continue;
                }

                if (craftingRecipe instanceof ShapelessRecipe shapelessRecipe) {
                    for (RecipeChoice recipeChoice : shapelessRecipe.getChoiceList()) {
                        if (recipeChoice instanceof RecipeChoice.ExactChoice choice) {
                            for (ItemStack item : choice.getChoices()) items.add(item.getType());
                            continue;
                        }

                        RecipeChoice.MaterialChoice choice = (RecipeChoice.MaterialChoice) recipeChoice;
                        items.addAll(choice.getChoices());
                    }
                    continue;
                }

                continue;
            }

            if (recipe instanceof CookingRecipe<?> cookingRecipe) {
                if (!cookingRecipe.getKey().namespace().equalsIgnoreCase("minecraft")) continue;


                items.add(cookingRecipe.getResult().getType());

                RecipeChoice recipeChoice = cookingRecipe.getInputChoice();
                if (recipeChoice instanceof RecipeChoice.ExactChoice choice) {
                    for (ItemStack item : choice.getChoices()) items.add(item.getType());
                    continue;
                }

                RecipeChoice.MaterialChoice choice = (RecipeChoice.MaterialChoice) recipeChoice;
                items.addAll(choice.getChoices());
            }
        }

        return new ArrayList<>(items);
    }
}
