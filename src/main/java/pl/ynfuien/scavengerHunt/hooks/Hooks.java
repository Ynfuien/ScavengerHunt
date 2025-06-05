package pl.ynfuien.scavengerHunt.hooks;

import org.bukkit.Bukkit;
import pl.ynfuien.scavengerHunt.ScavengerHunt;
import pl.ynfuien.scavengerHunt.hooks.vault.VaultHook;
import pl.ynfuien.ydevlib.messages.YLogger;

public class Hooks {
    public static void load(ScavengerHunt instance) {
        // Register Vault hook
        if (isPluginEnabled(Plugin.VAULT)) {
            new VaultHook();
            YLogger.info("[Hooks] Successfully enabled hook for Vault!");
        }
    }

    public static boolean isPluginEnabled(Plugin plugin) {
        return Bukkit.getPluginManager().isPluginEnabled(plugin.getName());
    }

    public enum Plugin {
        PAPI("PlaceholderAPI"),
        VAULT("Vault"),
        LUCKPERMS("LuckPerms");

        private final String name;
        Plugin(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }
    }
}
