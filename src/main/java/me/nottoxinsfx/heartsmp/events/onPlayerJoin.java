package me.nottoxinsfx.heartsmp.events;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

import me.nottoxinsfx.heartsmp.utils.YamlReader;

import java.util.Arrays;
import java.util.List;

import java.util.Random;

public class onPlayerJoin implements Listener {
    private final JavaPlugin plugin;
    private final YamlReader yamlReader;

    private final List<String> heartTypes = Arrays.asList(
            "EnergyHeart", "FireHeart", "LifeHeart", "SpeedHeart", "StrengthHeart", "WaterHeart"
    );

    public onPlayerJoin(JavaPlugin plugin) {
        this.plugin = plugin;
        this.yamlReader = new YamlReader(plugin);
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = (Player) event.getPlayer();
        String playerName = event.getPlayer().getName();
        String fileName = "hearts.yml";

        if (yamlReader.getValue("banned.yml", playerName) != null) {
            String banReason = ChatColor.AQUA + "Vitality SMP \n You are banned because you don't have any energy \n You need someone to revive you";
            player.kickPlayer(banReason);
            event.setJoinMessage(null);
        }

        Object playerHeart = yamlReader.getValue(fileName, playerName);
        if (playerHeart == null) {
            Random random = new Random();
            String selectedHeart = heartTypes.get(random.nextInt(heartTypes.size()));

            yamlReader.setValue(fileName, playerName, selectedHeart);
            switch (selectedHeart) {
                case "EnergyHeart":
                    ItemStack customItem = new ItemStack(Material.CARROT_ON_A_STICK);
                    ItemMeta meta = customItem.getItemMeta();
                    if (meta != null) {
                        meta.setDisplayName("§aEnergy Heart");
                        customItem.setItemMeta(meta);
                    }
                    player.getInventory().addItem(customItem);
                    break;
                case "FireHeart":
                    ItemStack fh = new ItemStack(Material.CARROT_ON_A_STICK);
                    ItemMeta metafh = fh.getItemMeta();
                    if (metafh != null) {
                        metafh.setDisplayName("§6Fire Heart");
                        fh.setItemMeta(metafh);
                    }
                    player.getInventory().addItem(fh);
                    break;
                case "LifeHeart":
                    ItemStack customItemlh = new ItemStack(Material.CARROT_ON_A_STICK);
                    ItemMeta metalh = customItemlh.getItemMeta();
                    if (metalh != null) {
                        metalh.setDisplayName("§5Life Heart");
                        customItemlh.setItemMeta(metalh);
                    }
                    player.getInventory().addItem(customItemlh);
                    break;
                case "SpeedHeart":
                    ItemStack customItemsph = new ItemStack(Material.CARROT_ON_A_STICK);
                    ItemMeta metasph = customItemsph.getItemMeta();
                    if (metasph != null) {
                        metasph.setDisplayName("§eSpeed Heart");
                        customItemsph.setItemMeta(metasph);
                    }
                    player.getInventory().addItem(customItemsph);
                    break;
                case "StrengthHeart":
                    ItemStack customItemsh = new ItemStack(Material.CARROT_ON_A_STICK);
                    ItemMeta metash = customItemsh.getItemMeta();
                    if (metash != null) {
                        metash.setDisplayName("§cStrength Heart");
                        customItemsh.setItemMeta(metash);
                    }
                    player.getInventory().addItem(customItemsh);
                    break;
                case "WaterHeart":
                    ItemStack customItemwh = new ItemStack(Material.CARROT_ON_A_STICK);
                    ItemMeta metawh = customItemwh.getItemMeta();
                    if (metawh != null) {
                        metawh.setDisplayName("§9Water Heart");
                        customItemwh.setItemMeta(metawh);
                    }
                    player.getInventory().addItem(customItemwh);
                    break;
            }
        }



        if (yamlReader.getValue("energy.yml", playerName) == null) {
            yamlReader.setValue("energy.yml", playerName, 10);
        }
    }
}
