package me.nottoxinsfx.heartsmp.commands;

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

public class giveri implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        Player player = (Player) commandSender;
        ItemStack customNetherStar = new ItemStack(Material.NETHER_STAR);
        ItemMeta meta = customNetherStar.getItemMeta();
        meta.setDisplayName("§eRevive player");
        customNetherStar.setItemMeta(meta);
        player.getInventory().addItem(customNetherStar);
        return true;
    }
}
