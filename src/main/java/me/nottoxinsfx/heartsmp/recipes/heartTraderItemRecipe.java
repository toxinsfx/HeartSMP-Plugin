package me.nottoxinsfx.heartsmp.recipes;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

public class heartTraderItemRecipe {
    public static void registerRecipe(JavaPlugin plugin) {
        ItemStack heartTraderItem = new ItemStack(Material.NETHER_STAR);

        ItemMeta meta = heartTraderItem.getItemMeta();
        meta.setDisplayName("§9Heart Trader");
        heartTraderItem.setItemMeta(meta);

        NamespacedKey recipeKey = new NamespacedKey(plugin, "heart_trader");

        ShapedRecipe traderItemRecipe = new ShapedRecipe(recipeKey, heartTraderItem);

        traderItemRecipe.shape(
                "DND",
                "DBD",
                "DND"
        );

        traderItemRecipe.setIngredient('N', Material.NETHERITE_BLOCK);
        traderItemRecipe.setIngredient('D', Material.DIAMOND_BLOCK);
        traderItemRecipe.setIngredient('B', Material.BEACON);

        Bukkit.addRecipe(traderItemRecipe);
    }

}
