package pl.ynfuien.scavengerHunt.commands.main.subcommands;

import org.bukkit.command.CommandSender;
import pl.ynfuien.scavengerHunt.ScavengerHunt;
import pl.ynfuien.scavengerHunt.commands.Subcommand;
import pl.ynfuien.ydevlib.utils.DoubleFormatter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class GiveSubcommand implements Subcommand {
    private final ScavengerHunt instance;

    public GiveSubcommand(ScavengerHunt instance) {
        this.instance = instance;
    }

    @Override
    public String permission() {
        return "ygenerators.admin."+name();
    }

    @Override
    public String name() {
        return "give";
    }

    @Override
    public void run(CommandSender sender, String[] args, HashMap<String, Object> placeholders) {
        if (args.length == 0) {
//            Lang.Message.COMMAND_GIVE_FAIL_NO_GENERATOR.send(sender, placeholders);
//            return;
        }


    }

    @Override
    public List<String> getTabCompletions(CommandSender sender, String[] args) {
        List<String> completions = new ArrayList<>();
        if (args.length > 4) return completions;

        String arg1 = args[0].toLowerCase();

        // Generator names
//        if (args.length == 1) {
//            for (Generator gene : generators.getAll().values()) {
//                String name = gene.getName();
//                if (name.startsWith(arg1)) completions.add(name);
//            }
//
//            return completions;
//        }

//        String arg2 = args[1].toLowerCase();
//
//        // Players
//        if (args.length == 2) {
//            Player player = sender instanceof Player p ? p : null;
//
//            for (Player p : Bukkit.getOnlinePlayers()) {
//                if (player != null && !player.canSee(p)) continue;
//
//                String name = p.getName();
//                if (name.toLowerCase().startsWith(arg2)) completions.add(name);
//            }
//
//            return completions;
//        }


        return completions;
    }
}
