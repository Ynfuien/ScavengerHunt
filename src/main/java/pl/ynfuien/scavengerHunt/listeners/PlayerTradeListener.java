package pl.ynfuien.scavengerHunt.listeners;

import io.papermc.paper.event.player.PlayerTradeEvent;
import org.bukkit.entity.AbstractVillager;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import pl.ynfuien.scavengerHunt.ScavengerHunt;
import pl.ynfuien.scavengerHunt.core.hunts.Hunt;
import pl.ynfuien.scavengerHunt.core.hunts.Hunts;
import pl.ynfuien.scavengerHunt.core.tasks.item.ItemTask;
import pl.ynfuien.scavengerHunt.core.tasks.item.ItemTasks;
import pl.ynfuien.scavengerHunt.core.tasks.trade.TradeTask;

public class PlayerTradeListener implements Listener {
    private final ScavengerHunt instance;
    private final Hunts hunts;
    private final ItemTasks itemTasks;

    public PlayerTradeListener(ScavengerHunt instance) {
        this.instance = instance;
        this.hunts = instance.getHunts();
        this.itemTasks = instance.getTasks().getItemTasks();
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onPlayerTrade(PlayerTradeEvent event) {
        Player player = event.getPlayer();

        Hunt hunt = hunts.getCurrentHunt(player);
        if (hunt == null) return;

        AbstractVillager av = event.getVillager();
        if (!(av instanceof Villager villager)) return;

        hunt.handleTask(TradeTask.class, villager.getProfession());
    }
}
