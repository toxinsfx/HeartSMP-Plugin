package me.nottoxinsfx.heartsmp.commands;

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

public class givesh implements CommandExecutor {
    private final JavaPlugin plugin;
    public givesh(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String s, @NotNull String[] args) {
        Player player = (Player) sender;
        ItemStack customItem = new ItemStack(Material.CARROT_ON_A_STICK);
        ItemMeta meta = customItem.getItemMeta();
        if (meta != null) {
            meta.setDisplayName("§cStrength Heart");
            customItem.setItemMeta(meta);
        }
        player.getInventory().addItem(customItem);
        return true;
    }
}
