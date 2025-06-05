package pl.ynfuien.scavengerHunt.core.tasks.ride;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Vehicle;
import pl.ynfuien.scavengerHunt.ScavengerHunt;
import pl.ynfuien.scavengerHunt.core.tasks.ITaskGenerator;
import pl.ynfuien.ydevlib.messages.YLogger;

import java.util.ArrayList;
import java.util.List;

public class RideTasks implements ITaskGenerator {
    private final ScavengerHunt instance;

    private boolean enabled = false;
    private final List<EntityType> vehicles = new ArrayList<>();

    public RideTasks(ScavengerHunt instance) {
        this.instance = instance;
    }

    public boolean load(ConfigurationSection config) {
        if (config == null) return false;

        enabled = config.getBoolean("enabled");
        if (!enabled) return true;

        YLogger.info("Loading ride-vehicle...");
        vehicles.clear();

        List<String> list = config.getStringList("list");
        for (String value : list) {
            EntityType vehicle = EntityType.fromName(value);
            if (vehicle == null) {
                YLogger.warn(String.format("Vehicle '%s' for the ride task is incorrect!", value));
                continue;
            }

            Class<? extends Entity> clazz = vehicle.getEntityClass();
            if (clazz == null) {
                YLogger.warn(String.format("Vehicle '%s' for the ride task is incorrect!", value));
                continue;
            }

            if (clazz.isInstance(Vehicle.class)) {
                YLogger.warn(String.format("Vehicle '%s' for the ride task is incorrect!", value));
                continue;
            }

            vehicles.add(vehicle);
        }

        return true;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public List<EntityType> getVehicles() {
        return vehicles;
    }


    @Override
    public RideTask createTask() {
        EntityType vehicle = vehicles.get(ScavengerHunt.randomBetween(0, vehicles.size()));
        return new RideTask(vehicle);
    }
}
