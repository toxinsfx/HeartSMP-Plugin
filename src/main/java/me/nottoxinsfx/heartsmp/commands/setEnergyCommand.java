package me.nottoxinsfx.heartsmp.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import me.nottoxinsfx.heartsmp.utils.YamlReader;

public class setEnergyCommand implements CommandExecutor {
    private final JavaPlugin plugin;
    private final YamlReader yamlReader;

    public setEnergyCommand(JavaPlugin plugin) {
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
            player.sendMessage(ChatColor.RED + "[-] Can't set energy to nothing. Please add arguments to command");
            return true;
        } else if (args.length == 2) {
            int value;
            try {
                value = Integer.parseInt(args[1]);
            } catch (NumberFormatException e) {
                player.sendMessage(ChatColor.RED + "[-] Invalid energy value. Please enter a valid integer.");
                return true;
            }
            String playerName = args[0];
            setEnergy(playerName, value);
            player.sendMessage(ChatColor.GREEN + "[+] Successfully set energy of " + playerName + " to " + value);
        } else {
            player.sendMessage(ChatColor.RED + "Usage: /setenergy [value] [player]");
        }
        return true;
    }
    private int setEnergy(String playerName, int value) {
        yamlReader.setValue("energy.yml", playerName, (Object) value);
        return -1;
    }
}
