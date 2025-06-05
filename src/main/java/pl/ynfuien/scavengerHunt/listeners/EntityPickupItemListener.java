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
import pl.ynfuien.ydevlib.messages.YLogger;

public class EntityPickupItemListener implements Listener {
    private final ScavengerHunt instance;
    private final Hunts hunts;
    private final ItemTasks itemTasks;

    public EntityPickupItemListener(ScavengerHunt instance) {
        this.instance = instance;
        this.hunts = instance.getHunts();
        this.itemTasks = instance.getTasks().getItemTasks();
    }

//    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
//    public void onPlayerPickupItem(EntityPickupItemEvent event) {
//        if (!(event.getEntity() instanceof Player player)) return;
//
//        Hunt hunt = hunts.getCurrentHunt(player);
//        if (hunt == null) return;
//
//        Item item = event.getItem();
//        ItemStack itemStack = event.getItem().getItemStack();
//        boolean result = hunt.handleTask(ItemTask.class, itemStack.getType());
//
//        if (!itemTasks.isConsume()) return;
//        if (!result) return;
//
//        if (itemStack.getAmount() == 1) {
//            item.setItemStack(ItemStack.empty());
//            return;
//        }
//    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerPickupItem(PlayerInventorySlotChangeEvent event) {
        Player player = event.getPlayer();

        Hunt hunt = hunts.getCurrentHunt(player);
        if (hunt == null) return;

        ItemStack itemStack = event.getNewItemStack();
        boolean result = hunt.handleTask(ItemTask.class, itemStack.getType());

        if (!itemTasks.isConsume()) return;
        if (!result) return;

        YLogger.debug("Consume!");
        PlayerInventory inv = player.getInventory();
        inv.removeItemAnySlot(itemStack.asOne());
    }
}
