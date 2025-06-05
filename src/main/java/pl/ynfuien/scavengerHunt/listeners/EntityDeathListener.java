package pl.ynfuien.scavengerHunt.listeners;

import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import pl.ynfuien.scavengerHunt.ScavengerHunt;
import pl.ynfuien.scavengerHunt.core.hunts.Hunt;
import pl.ynfuien.scavengerHunt.core.hunts.Hunts;
import pl.ynfuien.scavengerHunt.core.tasks.mob.MobTask;

public class EntityDeathListener implements Listener {
    private final ScavengerHunt instance;
    private final Hunts hunts;

    public EntityDeathListener(ScavengerHunt instance) {
        this.instance = instance;
        this.hunts = instance.getHunts();
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onPlayerKillMob(EntityDeathEvent event) {
        Entity damager = event.getDamageSource().getCausingEntity();
        if (damager == null) return;
        if (!(damager instanceof Player player)) return;

        Hunt hunt = hunts.getCurrentHunt(player);
        if (hunt == null) return;

        EntityType mob = event.getEntity().getType();
        hunt.handleTask(MobTask.class, mob);
    }
}
