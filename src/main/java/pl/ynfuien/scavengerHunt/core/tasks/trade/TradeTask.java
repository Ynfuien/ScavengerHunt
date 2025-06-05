package pl.ynfuien.scavengerHunt.core.tasks.trade;

import org.bukkit.entity.Villager;
import pl.ynfuien.scavengerHunt.core.tasks.Task;

public class TradeTask extends Task<Villager.Profession> {
    public TradeTask(Villager.Profession profession) {
        super(profession);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (obj instanceof TradeTask task) return task.getGoal().equals(this.goal);

        return super.equals(obj);
    }
}
