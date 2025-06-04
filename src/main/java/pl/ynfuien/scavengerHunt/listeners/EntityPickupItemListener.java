package pl.ynfuien.scavengerHunt.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.inventory.ItemStack;
import pl.ynfuien.scavengerHunt.ScavengerHunt;
import pl.ynfuien.scavengerHunt.core.hunts.Hunt;
import pl.ynfuien.scavengerHunt.core.hunts.Hunts;

public class EntityPickupItemListener implements Listener {
    private final ScavengerHunt instance;
    private final Hunts hunts;

    public EntityPickupItemListener(ScavengerHunt instance) {
        this.instance = instance;
        this.hunts = instance.getHunts();
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onPlayerPickupItem(EntityPickupItemEvent event) {
        if (!(event.getEntity() instanceof Player player)) return;

        Hunt hunt = hunts.getHunt(player);
        if (hunt == null) return;

        ItemStack item = event.getItem().getItemStack();
        hunt.itemPickedUp(item.getType());
    }
}
