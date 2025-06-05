package pl.ynfuien.scavengerHunt.listeners;

import io.papermc.paper.event.player.PlayerInventorySlotChangeEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import pl.ynfuien.scavengerHunt.ScavengerHunt;
import pl.ynfuien.scavengerHunt.core.hunts.Hunt;
import pl.ynfuien.scavengerHunt.core.hunts.Hunts;
import pl.ynfuien.scavengerHunt.core.tasks.item.ItemTask;
import pl.ynfuien.scavengerHunt.core.tasks.item.ItemTasks;

public class PlayerInventorySlotChangeListener implements Listener {
    private final ScavengerHunt instance;
    private final Hunts hunts;
    private final ItemTasks itemTasks;

    public PlayerInventorySlotChangeListener(ScavengerHunt instance) {
        this.instance = instance;
        this.hunts = instance.getHunts();
        this.itemTasks = instance.getTasks().getItemTasks();
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerInventorySlotChange(PlayerInventorySlotChangeEvent event) {
        Player player = event.getPlayer();

        Hunt hunt = hunts.getCurrentHunt(player);
        if (hunt == null) return;

        ItemStack itemStack = event.getNewItemStack();
        boolean result = hunt.handleTask(ItemTask.class, itemStack.getType());

        if (!itemTasks.isConsume()) return;
        if (!result) return;

        PlayerInventory inv = player.getInventory();
        inv.removeItemAnySlot(itemStack.asOne());
    }
}
