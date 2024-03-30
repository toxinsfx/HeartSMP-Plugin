package me.nottoxinsfx.heartsmp.recipes;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

public class reviveItemRecipe {
    public static void registerRecipe(JavaPlugin plugin) {
        ItemStack revivePlayerItem = new ItemStack(Material.NETHER_STAR);

        ItemMeta meta = revivePlayerItem.getItemMeta();
        meta.setDisplayName("§eRevive player");
        revivePlayerItem.setItemMeta(meta);

        NamespacedKey recipeKey = new NamespacedKey(plugin, "revive_player");

        ShapedRecipe revivePlayerRecipe = new ShapedRecipe(recipeKey, revivePlayerItem);

        revivePlayerRecipe.shape(
                "DDD",
                "DND",
                "DDD"
        );

        revivePlayerRecipe.setIngredient('D', Material.DIAMOND_BLOCK);
        revivePlayerRecipe.setIngredient('N', Material.NETHERITE_BLOCK);

        Bukkit.addRecipe(revivePlayerRecipe);
    }

}
