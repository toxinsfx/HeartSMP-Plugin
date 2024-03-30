package me.nottoxinsfx.heartsmp.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import me.nottoxinsfx.heartsmp.utils.YamlReader;

public class checkheart implements CommandExecutor {
    private final JavaPlugin plugin;
    private final YamlReader yamlReader;

    public checkheart(JavaPlugin plugin) {
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
            player.sendMessage(ChatColor.AQUA + "Specify a player");
        } else if (args.length == 1) {
            String playerName = args[0];
            String energy = getEnergy(playerName);
            if (energy == "Failed") {
                player.sendMessage(ChatColor.AQUA + "" + playerName + " not found");
            } else {
                player.sendMessage(ChatColor.AQUA + "" + playerName + " has " + energy + "");
            }
        } else {
            player.sendMessage("Usage: /checkheart [player]");
        }
        return true;
    }

    private String getEnergy(String playerName) {
        Object value = yamlReader.getValue("hearts.yml", playerName);
        if (value != null) {
            return (String) value;
        }
        return "Failed";
    }
}
