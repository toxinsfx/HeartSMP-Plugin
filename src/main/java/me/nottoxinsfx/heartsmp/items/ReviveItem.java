package me.nottoxinsfx.heartsmp.items;

import me.nottoxinsfx.heartsmp.utils.YamlReader;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Map;

public class ReviveItem implements Listener {
    private final JavaPlugin plugin;
    private final YamlReader yamlReader;

    public ReviveItem(JavaPlugin plugin) {
        this.plugin = plugin;
        this.yamlReader = new YamlReader(plugin);
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    public void giveCustomNetherStar(Player player) {
        ItemStack customNetherStar = new ItemStack(Material.NETHER_STAR);
        ItemMeta meta = customNetherStar.getItemMeta();
        meta.setDisplayName("Revive player");
        customNetherStar.setItemMeta(meta);
        player.getInventory().addItem(customNetherStar);
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        ItemStack item = event.getItem();
        if (item != null && item.getType() == Material.NETHER_STAR && item.hasItemMeta()) {
            if (item.getItemMeta().getDisplayName().equals("§eRevive player")) {
                openBannedPlayersGUI(player);
            }
        }
    }

    public void openBannedPlayersGUI(Player player) {
        Inventory gui = Bukkit.createInventory(player, 27, "Revive player");
        Map<String, Object> bannedPlayersData = yamlReader.loadYaml("banned.yml");
        if (bannedPlayersData != null) {
            for (String playerName : bannedPlayersData.keySet()) {
                ItemStack playerHead = getPlayerHead(playerName);
                gui.addItem(playerHead);
            }
        }
        player.openInventory(gui);
    }

    public ItemStack getPlayerHead(String playerName) {
        ItemStack playerHead = new ItemStack(Material.PLAYER_HEAD);
        ItemMeta meta = playerHead.getItemMeta();
        meta.setDisplayName(playerName);
        playerHead.setItemMeta(meta);
        return playerHead;
    }

    @EventHandler
    public void onInventoryClick(org.bukkit.event.inventory.InventoryClickEvent event) {
        if (event.getView().getTitle().equals("Revive player")) {
            event.setCancelled(true);
            ItemStack clickedItem = event.getCurrentItem();
            if (clickedItem != null && clickedItem.getType() == Material.PLAYER_HEAD && clickedItem.hasItemMeta()) {
                String playerName = clickedItem.getItemMeta().getDisplayName();
                unbanPlayer(playerName);
                setPlayerEnergy(playerName, 10);
                event.getWhoClicked().closeInventory();
                event.getWhoClicked().sendMessage(ChatColor.GREEN + "Revived " + playerName +"!");
                Bukkit.getServer().broadcastMessage(ChatColor.GREEN + playerName + " got revived by " + event.getWhoClicked().getName());
                ItemStack customNetherStar = new ItemStack(Material.NETHER_STAR);
                ItemMeta meta = customNetherStar.getItemMeta();
                meta.setDisplayName("§eRevive player");
                customNetherStar.setItemMeta(meta);
                event.getWhoClicked().getInventory().remove(customNetherStar);
            }
        }
    }

    public void unbanPlayer(String playerName) {
        yamlReader.removeValue("banned.yml", playerName);
        Bukkit.getBanList(org.bukkit.BanList.Type.NAME).pardon(playerName);
    }

    public void setPlayerEnergy(String playerName, int energy) {
        yamlReader.setValue("energy.yml", playerName, energy);
    }
}
