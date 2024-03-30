package me.nottoxinsfx.heartsmp.events;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.inventory.ItemStack;

public class onPlayerDrop implements Listener {

    @EventHandler
    public void onPlayerDrop(PlayerDropItemEvent event) {
        Player player = event.getPlayer();
        ItemStack droppedItem = event.getItemDrop().getItemStack();

        if (isHeartItem(droppedItem)) {
            event.setCancelled(true);
        }
    }
    private boolean isHeartItem(ItemStack item) {
        String displayName = item.getItemMeta().getDisplayName();
        if (displayName != null) {
            return displayName.equals("§aEnergy Heart")
                    || displayName.equals("§6Fire Heart")
                    || displayName.equals("§5Life Heart")
                    || displayName.equals("§eSpeed Heart")
                    || displayName.equals("§cStrength Heart")
                    || displayName.equals("§9Water Heart");
        }
        return false;
    }
}
