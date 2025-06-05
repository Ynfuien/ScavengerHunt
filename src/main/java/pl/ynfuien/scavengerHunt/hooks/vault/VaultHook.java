package pl.ynfuien.scavengerHunt.hooks.vault;

import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.ServicesManager;

public class VaultHook {
    private static Economy economy = null;

    public VaultHook() {
        ServicesManager manager = Bukkit.getServer().getServicesManager();

        RegisteredServiceProvider<Economy> economyRsp = manager.getRegistration(Economy.class);
        if (economyRsp != null) economy = economyRsp.getProvider();
    }


    public static boolean isEconomy() {
        return economy != null;
    }
    public static Economy getEconomy() {
        return economy;
    }
}
