package me.nottoxinsfx.heartsmp.events;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import me.nottoxinsfx.heartsmp.utils.YamlReader;

public class actionBarEvent implements Listener {
    private final JavaPlugin plugin;
    private final YamlReader yamlReader;

    public actionBarEvent(JavaPlugin plugin) {
        this.plugin = plugin;
        this.yamlReader = new YamlReader(plugin);

        new BukkitRunnable() {
            @Override
            public void run() {
                for (Player player : plugin.getServer().getOnlinePlayers()) {
                    updateActionBar(player);
                }
            }
        }.runTaskTimer(plugin, 0, 200);

        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        updateActionBar(player);
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        clearActionBar(player);
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        Player player = event.getEntity();
        updateActionBar(player);
    }

    private void updateActionBar(Player player) {
        new BukkitRunnable() {
            @Override
            public void run() {
                int energy = getEnergy(player);
            player.sendActionBar(ChatColor.AQUA + "" + energy + " ⚡");
            }
        }.runTaskTimer(plugin, 0, 20);
    }


    private void clearActionBar(Player player) {
        player.sendActionBar("");
    }

    private int getEnergy(Player player) {
        String playerName = player.getName();
        Object value = yamlReader.getValue("energy.yml", playerName);
        if (value != null && value instanceof Integer) {
            return (int) value;
        }
        return 0;
    }
}
