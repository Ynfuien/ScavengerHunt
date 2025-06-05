package pl.ynfuien.scavengerHunt.commands.main;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import net.kyori.adventure.translation.Translatable;
import org.bukkit.Keyed;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pl.ynfuien.scavengerHunt.Lang;
import pl.ynfuien.scavengerHunt.ScavengerHunt;
import pl.ynfuien.scavengerHunt.commands.Subcommand;
import pl.ynfuien.scavengerHunt.commands.main.subcommands.AbortSubcommand;
import pl.ynfuien.scavengerHunt.commands.main.subcommands.AssignSubcommand;
import pl.ynfuien.scavengerHunt.commands.main.subcommands.ReloadSubcommand;
import pl.ynfuien.scavengerHunt.core.hunts.Hunt;
import pl.ynfuien.scavengerHunt.core.hunts.Hunts;
import pl.ynfuien.scavengerHunt.core.tasks.Task;
import pl.ynfuien.scavengerHunt.core.tasks.biome.BiomeTask;
import pl.ynfuien.scavengerHunt.core.tasks.item.ItemTask;
import pl.ynfuien.scavengerHunt.core.tasks.mob.MobTask;
import pl.ynfuien.scavengerHunt.core.tasks.ride.RideTask;
import pl.ynfuien.scavengerHunt.core.tasks.trade.TradeTask;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class MainCommand implements CommandExecutor, TabCompleter {
    private final ScavengerHunt instance;
    private final Hunts hunts;
    private final Subcommand[] subcommands;

    public MainCommand(ScavengerHunt instance) {
        this.instance = instance;
        this.hunts = instance.getHunts();

        this.subcommands = new Subcommand[] {
                new AssignSubcommand(instance),
                new AbortSubcommand(instance),
                new ReloadSubcommand(instance),
        };
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        HashMap<String, Object> placeholders = new HashMap<>() {{put("command", label);}};

        if (args.length == 0) {
            info(sender, placeholders);
            return true;
        }


        // Loop through and check every subcommand
        String arg1 = args[0].toLowerCase();
        for (Subcommand subcommand : subcommands) {
            if (!subcommand.name().equals(arg1)) continue;
            if (!sender.hasPermission(subcommand.permission())) break;

            String[] argsLeft = Arrays.copyOfRange(args, 1, args.length);
            subcommand.run(sender, argsLeft, placeholders);
            return true;
        }

        info(sender, placeholders);
        return true;
    }

    private void info(CommandSender sender, HashMap<String, Object> placeholders) {
        if (!(sender instanceof Player player)) {
            Lang.Message.COMMAND_USAGE.send(sender, placeholders);
            return;
        }


        Hunt hunt = hunts.getCurrentHunt(player);
        if (hunt == null) {
            Lang.Message.COMMAND_HUNT_INFO_NO_ASSIGNMENT.send(sender);
            return;
        }

        List<Task<?>> tasks = hunt.getTasks();

        placeholders.put("completed-task-count", hunt.getCompletedTaskCount());
        placeholders.put("task-count", tasks.size());
        Lang.Message.COMMAND_HUNT_INFO_HEADER.send(sender, placeholders);

        for (Task<?> task : tasks) {
            if (task instanceof ItemTask itemTask) {
                Material item = itemTask.getGoal();
                String displayName = LegacyComponentSerializer.legacyAmpersand().serialize((new ItemStack(item).effectiveName().color(null)));

                placeholders.put("item-display-name", displayName);
                placeholders.put("item-display-name-lower-case", displayName.toLowerCase());
                placeholders.put("item-display-name-upper-case", displayName.toUpperCase());
                placeholders.put("item-name", item.name());

                if (itemTask.isCompleted()) {
                    Lang.Message.COMMAND_HUNT_INFO_TASK_ITEM_COMPLETED.send(player, placeholders);
                    continue;
                }

                Lang.Message.COMMAND_HUNT_INFO_TASK_ITEM.send(player, placeholders);
                continue;
            }

            String placeholderName;
            Lang.Message completed;
            Lang.Message ongoing;

            switch (task) {
                case MobTask ignored -> {
                    placeholderName = "mob";
                    completed = Lang.Message.COMMAND_HUNT_INFO_TASK_MOB_COMPLETED;
                    ongoing = Lang.Message.COMMAND_HUNT_INFO_TASK_MOB;
                }
                case BiomeTask ignored -> {
                    placeholderName = "biome";
                    completed = Lang.Message.COMMAND_HUNT_INFO_TASK_BIOME_COMPLETED;
                    ongoing = Lang.Message.COMMAND_HUNT_INFO_TASK_BIOME;
                }
                case TradeTask ignored -> {
                    placeholderName = "profession";
                    completed = Lang.Message.COMMAND_HUNT_INFO_TASK_TRADE_COMPLETED;
                    ongoing = Lang.Message.COMMAND_HUNT_INFO_TASK_TRADE;
                }
                case RideTask ignored -> {
                    placeholderName = "vehicle";
                    completed = Lang.Message.COMMAND_HUNT_INFO_TASK_RIDE_COMPLETED;
                    ongoing = Lang.Message.COMMAND_HUNT_INFO_TASK_RIDE;
                }
                default -> throw new IllegalStateException("Unexpected value: " + task);
            }


            Keyed goal = (Keyed) task.getGoal();

            String displayName = LegacyComponentSerializer.legacyAmpersand().serialize(Component.translatable(((Translatable) goal).translationKey()));
            placeholders.put(placeholderName+"-display-name", displayName);
            placeholders.put(placeholderName+"-display-name-lower-case", displayName.toLowerCase());
            placeholders.put(placeholderName+"-display-name-upper-case", displayName.toUpperCase());
            placeholders.put(placeholderName+"-name", goal.key().value());

            if (task.isCompleted()) {
                completed.send(player, placeholders);
                continue;
            }

            ongoing.send(player, placeholders);
        }

        int minutesLeft = hunt.getTimeLeft();
        placeholders.put("hours-left", minutesLeft / 60);
        placeholders.put("minutes-left", minutesLeft % 60);
        Lang.Message.COMMAND_HUNT_INFO_FOOTER.send(sender, placeholders);
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        return tabCompleteSubcommands(sender, subcommands, args);
    }

    public static List<String> tabCompleteSubcommands(CommandSender sender, Subcommand[] subcommands, String[] args) {
        List<String> completions = new ArrayList<>();

        // Get commands the sender has permissions for
        List<Subcommand> canUse = Arrays.stream(subcommands).filter(cmd -> sender.hasPermission(cmd.permission())).toList();
        if (canUse.isEmpty()) return completions;

        //// Tab completion for subcommand names
        String arg1 = args[0].toLowerCase();
        if (args.length == 1) {
            for (Subcommand cmd : canUse) {
                String name = cmd.name();

                if (name.startsWith(args[0])) {
                    completions.add(name);
                }
            }

            return completions;
        }

        //// Tab completion for subcommand arguments

        // Get provided command in first arg
        Subcommand subcommand = canUse.stream().filter(cmd -> cmd.name().equals(arg1)).findAny().orElse(null);
        if (subcommand == null) return completions;

        // Get completions from provided command and return them if there are any
        List<String> subcommandCompletions = subcommand.getTabCompletions(sender, Arrays.copyOfRange(args, 1, args.length));
        if (subcommandCompletions != null) return subcommandCompletions;

        return completions;
    }
}
