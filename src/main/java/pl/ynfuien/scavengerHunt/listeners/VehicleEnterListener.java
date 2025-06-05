package pl.ynfuien.scavengerHunt.listeners;

import org.bukkit.entity.Player;
import org.bukkit.entity.Vehicle;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.vehicle.VehicleEnterEvent;
import pl.ynfuien.scavengerHunt.ScavengerHunt;
import pl.ynfuien.scavengerHunt.core.hunts.Hunt;
import pl.ynfuien.scavengerHunt.core.hunts.Hunts;
import pl.ynfuien.scavengerHunt.core.tasks.ride.RideTask;

public class VehicleEnterListener implements Listener {
    private final ScavengerHunt instance;
    private final Hunts hunts;

    public VehicleEnterListener(ScavengerHunt instance) {
        this.instance = instance;
        this.hunts = instance.getHunts();
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onVehicleEnter(VehicleEnterEvent event) {
        if (!(event.getEntered() instanceof Player player)) return;

        Hunt hunt = hunts.getCurrentHunt(player);
        if (hunt == null) return;

        Vehicle vehicle = event.getVehicle();
        hunt.handleTask(RideTask.class, vehicle.getType());
    }
}
