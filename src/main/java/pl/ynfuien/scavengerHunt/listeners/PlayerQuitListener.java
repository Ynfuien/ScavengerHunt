package pl.ynfuien.scavengerHunt.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import pl.ynfuien.scavengerHunt.ScavengerHunt;
import pl.ynfuien.scavengerHunt.core.hunts.Hunts;

public class PlayerQuitListener implements Listener {
    private final ScavengerHunt instance;
    private final Hunts hunts;

    public PlayerQuitListener(ScavengerHunt instance) {
        this.instance = instance;
        this.hunts = instance.getHunts();
    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();

        hunts.removeHuntFromCache(player);
    }
}
