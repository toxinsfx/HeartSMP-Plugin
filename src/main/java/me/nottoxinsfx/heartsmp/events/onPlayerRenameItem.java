package me.nottoxinsfx.heartsmp.events;

import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.PrepareAnvilEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;
import java.util.List;

public class onPlayerRenameItem implements Listener {
    private final List<String> heartTypes = Arrays.asList(
            "§aEnergy Heart", "§6Fire Heart", "§5Life Heart", "§eSpeed Heart", "§cStrength Heart", "§9Water Heart"
    );

    @EventHandler
    public void onPlayerRenameItem(PrepareAnvilEvent event) {
        if(event.getResult() != null && event.getResult().hasItemMeta() && event.getInventory().getRenameText() != ""){
            ItemStack result = event.getResult();
            ItemMeta resultMeta = result.getItemMeta();

            ItemStack failed = new ItemStack(result.getType(), result.getAmount());

            for (String heartName : heartTypes) {
                if (resultMeta.getDisplayName().equals(heartName)) {
                    event.setResult(failed);
                    return;
                }
            }
            String nameColored = ChatColor.translateAlternateColorCodes('&', event.getInventory().getRenameText());
            resultMeta.setDisplayName(nameColored);
            result.setItemMeta(resultMeta);
        }
    }
}
