package me.nottoxinsfx.heartsmp.events;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.plugin.java.JavaPlugin;

import me.nottoxinsfx.heartsmp.utils.YamlReader;

public class onPlayerKill implements Listener {
    private final JavaPlugin plugin;
    private final YamlReader yamlReader;

    public onPlayerKill(JavaPlugin plugin) {
        this.plugin = plugin;
        this.yamlReader = new YamlReader(plugin);
    }

    @EventHandler
    public void onPlayerKill(EntityDeathEvent event) {
        if (event.getEntity() instanceof Player) {
            Player killed = (Player) event.getEntity();
            Player killer = killed.getKiller();

            if (killer != null) {
                String killerName = killer.getName();
                int currentEnergy = (int) yamlReader.getValue("energy.yml", killerName);
                int newEnergy = currentEnergy + 1;
                yamlReader.setValue("energy.yml", killerName, newEnergy);
            }
        }
    }
}
