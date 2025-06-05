package pl.ynfuien.scavengerHunt.commands.main.subcommands;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import pl.ynfuien.scavengerHunt.Lang;
import pl.ynfuien.scavengerHunt.ScavengerHunt;
import pl.ynfuien.scavengerHunt.commands.Subcommand;
import pl.ynfuien.scavengerHunt.core.hunts.Hunt;
import pl.ynfuien.scavengerHunt.core.hunts.Hunts;
import pl.ynfuien.ydevlib.utils.CommonPlaceholders;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class AssignSubcommand implements Subcommand {
    private final ScavengerHunt instance;
    private final Hunts hunts;

    public AssignSubcommand(ScavengerHunt instance) {
        this.instance = instance;
        this.hunts = instance.getHunts();
    }

    @Override
    public String permission() {
        return "scavengerhunt.command."+name();
    }

    @Override
    public String name() {
        return "assign";
    }

    @Override
    public void run(CommandSender sender, String[] args, HashMap<String, Object> placeholders) {
        if (args.length == 0) {
            Lang.Message.COMMAND_HUNT_ASSIGN_USAGE.send(sender, placeholders);
            return;
        }

        String arg1 = args[0];
        Player player = Bukkit.getPlayer(arg1);
        if (player == null) {
            Lang.Message.COMMAND_HUNT_ASSIGN_NO_PLAYER.send(sender, placeholders);
            return;
        }

        Hunt hunt = hunts.getCurrentHunt(player);
        if (hunt != null) hunt.finishTheHunt(false);

        hunts.createNewHunt(player);

        CommonPlaceholders.setPlayer(placeholders, player);
        Lang.Message.COMMAND_HUNT_ASSIGN_SUCCESS.send(sender, placeholders);
    }

    @Override
    public List<String> getTabCompletions(CommandSender sender, String[] args) {
        List<String> completions = new ArrayList<>();
        if (args.length > 1) return completions;

        String arg1 = args[0].toLowerCase();

        Player player = sender instanceof Player p ? p : null;
        for (Player p : Bukkit.getOnlinePlayers()) {
            if (player != null && !player.canSee(p)) continue;

            String name = p.getName();
            if (name.toLowerCase().startsWith(arg1)) completions.add(name);
        }

        return completions;
    }
}
