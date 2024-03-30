package me.nottoxinsfx.heartsmp.events;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Iterator;
import java.util.List;

import me.nottoxinsfx.heartsmp.utils.YamlReader;

public class onPlayerDeath implements Listener {
    private final JavaPlugin plugin;
    private final YamlReader yamlReader;

    public onPlayerDeath(JavaPlugin plugin) {
        this.plugin = plugin;
        this.yamlReader = new YamlReader(plugin);
    }

    private boolean isSpecialHeart(ItemStack item) {
        if (item != null && item.hasItemMeta() && item.getItemMeta().hasDisplayName()) {
            String displayName = item.getItemMeta().getDisplayName();
            return displayName.equals("§aEnergy Heart")
                    || displayName.equals("§6Fire Heart")
                    || displayName.equals("§5Life Heart")
                    || displayName.equals("§eSpeed Heart")
                    || displayName.equals("§cStrength Heart")
                    || displayName.equals("§9Water Heart");
        }
        return false;
    }


    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        Player player = event.getEntity();
        Player killer = event.getEntity().getKiller();
        String playerName = player.getName();

        if (killer != null) {
            int currentEnergy = (int) yamlReader.getValue("energy.yml", playerName);
            if (currentEnergy > 0) {
                int newEnergy = currentEnergy - 1;
                yamlReader.setValue("energy.yml", playerName, newEnergy);
            }
        }

        int energyValue = (int) yamlReader.getValue("energy.yml", playerName);
        if (energyValue == 0) {
            String banReason = ChatColor.AQUA + "Heart SMP \n You got banned because you lost all your energy";
            Bukkit.getBanList(org.bukkit.BanList.Type.NAME).addBan(playerName, banReason, null, null);
            player.kickPlayer(banReason);
            yamlReader.createValue("banned.yml", playerName);
        }

        List<ItemStack> drops = event.getDrops();
        Iterator<ItemStack> iterator = drops.iterator();
        while (iterator.hasNext()) {
            ItemStack drop = iterator.next();
            if (isSpecialHeart(drop)) {
                iterator.remove();
            }
        }

    }

}
