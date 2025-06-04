package pl.ynfuien.scavengerHunt.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import pl.ynfuien.scavengerHunt.ScavengerHunt;
import pl.ynfuien.scavengerHunt.core.hunts.Hunt;
import pl.ynfuien.scavengerHunt.core.hunts.Hunts;

public class PlayerJoinListener implements Listener {
    private final ScavengerHunt instance;
    private final Hunts hunts;

    public PlayerJoinListener(ScavengerHunt instance) {
        this.instance = instance;
        this.hunts = instance.getHunts();
    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        Hunt hunt = hunts.getHunt(player);
        if (hunt == null) return;


    }
}
