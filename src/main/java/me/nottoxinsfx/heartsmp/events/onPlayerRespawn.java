package me.nottoxinsfx.heartsmp.events;

import me.nottoxinsfx.heartsmp.utils.YamlReader;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Map;

public class onPlayerRespawn implements Listener {
    private final JavaPlugin plugin;
    private final YamlReader yamlReader;

    public onPlayerRespawn(JavaPlugin plugin) {
        this.plugin = plugin;
        this.yamlReader = new YamlReader(plugin);
    }

    private ItemStack getHeartItem(String heartType) {
        String displayName;
        switch (heartType) {
            case "EnergyHeart":
                displayName = "§aEnergy Heart";
                break;
            case "FireHeart":
                displayName = "§6Fire Heart";
                break;
            case "LifeHeart":
                displayName = "§5Life Heart";
                break;
            case "SpeedHeart":
                displayName = "§eSpeed Heart";
                break;
            case "StrengthHeart":
                displayName = "§cStrength Heart";
                break;
            case "WaterHeart":
                displayName = "§9Water Heart";
                break;
            default:
                return null;
        }

        ItemStack heartItem = new ItemStack(Material.CARROT_ON_A_STICK);
        ItemMeta meta = heartItem.getItemMeta();
        meta.setDisplayName(displayName);
        heartItem.setItemMeta(meta);

        return heartItem;
    }


    @EventHandler
    public void onPlayerRespawn(PlayerRespawnEvent event) {
        Player player = event.getPlayer();
        Player killer = event.getPlayer().getKiller();
        String playerName = player.getName();

        int energyValue = (int) yamlReader.getValue("energy.yml", playerName);
        if (energyValue == 0) {
            String banReason = ChatColor.AQUA + "Energy SMP \n You got banned because you lost all your energy";
            Bukkit.getBanList(org.bukkit.BanList.Type.NAME).addBan(playerName, banReason, null, null);
            player.kickPlayer(banReason);
            yamlReader.createValue("banned.yml", playerName);
        }

        String playerHeart = (String) yamlReader.getValue("hearts.yml", playerName);

        if (playerHeart == null) {
            Map<String, Object> heartsData = yamlReader.loadYaml("hearts.yml");
            if (heartsData != null && heartsData.containsKey(playerName)) {
                playerHeart = (String) heartsData.get(playerName);
            } else {
                playerHeart = "EnergyHeart";
                yamlReader.setValue("hearts.yml", playerName, playerHeart);
            }
        }
        ItemStack heartItem = getHeartItem(playerHeart);
        if (heartItem != null) {
            player.getInventory().addItem(heartItem);
        }


    }
}
