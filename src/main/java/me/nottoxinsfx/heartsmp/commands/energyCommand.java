package me.nottoxinsfx.heartsmp.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import me.nottoxinsfx.heartsmp.utils.YamlReader;

public class energyCommand implements CommandExecutor {
    private final JavaPlugin plugin;
    private final YamlReader yamlReader;

    public energyCommand(JavaPlugin plugin) {
        this.plugin = plugin;
        this.yamlReader = new YamlReader(plugin);
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String s, @NotNull String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("[-] Only players can use this command.");
            return true;
        }

        Player player = (Player) sender;
        if (args.length == 0) {
            int energy = getEnergy(player.getName());
            player.sendMessage(ChatColor.AQUA + "You have " + energy + " energy.");
        } else if (args.length == 1) {
            String playerName = args[0];
            int energy = getEnergy(playerName);
            if (energy == -1) {
                player.sendMessage(ChatColor.AQUA + "" + playerName + " not found");
            } else {
                player.sendMessage(ChatColor.AQUA + "" + playerName + " has " + energy + " energy.");
            }
        } else {
            player.sendMessage("Usage: /energy [player]");
        }
        return true;
    }

    private int getEnergy(String playerName) {
        Object value = yamlReader.getValue("energy.yml", playerName);
        if (value != null && value instanceof Integer) {
            return (int) value;
        }
        return -1;
    }
}
