package me.nottoxinsfx.heartsmp.items;

import me.nottoxinsfx.heartsmp.utils.YamlReader;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class TraderItem implements Listener {
    private final List<String> heartTypes = Arrays.asList(
            "Energy Heart", "Fire Heart", "Life Heart", "Speed Heart", "Strength Heart", "Water Heart"
    );

    private final YamlReader yamlReader;

    public TraderItem(YamlReader yamlReader) {
        this.yamlReader = yamlReader;
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();

        if (event.getItem() != null && event.getAction().isRightClick() && verifyItem(event, "§9Heart Trader")) {
            String chosenHeart = getRandomHeart();

            removeTraderItem(player);
            removeCarrotOnStick(player);
            // Overwrite player's heart with the chosen heart
            overwritePlayerHeart(player.getName(), chosenHeart);


            // Give the player the new carrot on a stick with the chosen heart
            giveNewCarrotOnStick(player, chosenHeart);
        }
    }

    private void giveNewCarrotOnStick(Player player, String chosenHeart) {
        ItemStack newCarrotOnStick = new ItemStack(Material.CARROT_ON_A_STICK);
        ItemMeta newMeta = newCarrotOnStick.getItemMeta();
        if (newMeta != null) {
            // Customize the display name based on the chosen heart
            switch (chosenHeart) {
                case "Energy Heart":
                    newMeta.setDisplayName("§a" + chosenHeart);
                    break;
                case "Fire Heart":
                    newMeta.setDisplayName("§6" + chosenHeart);
                    break;
                case "Life Heart":
                    newMeta.setDisplayName("§5" + chosenHeart);
                    break;
                case "Speed Heart":
                    newMeta.setDisplayName("§e" + chosenHeart);
                    break;
                case "Strength Heart":
                    newMeta.setDisplayName("§c" + chosenHeart);
                    break;
                case "Water Heart":
                    newMeta.setDisplayName("§9" + chosenHeart);
                    break;
                default:
                    newMeta.setDisplayName(chosenHeart);
                    break;
            }
            newCarrotOnStick.setItemMeta(newMeta);
        }
        player.getInventory().addItem(newCarrotOnStick);
    }

    private void removeCarrotOnStick(Player player) {
        ItemStack[] playerInventory = player.getInventory().getContents();
        for (ItemStack item : playerInventory) {
            if (item != null && item.getType() == Material.CARROT_ON_A_STICK) {
                player.getInventory().remove(item);
            }
        }
        if (player.getInventory().getItemInOffHand().getType() == Material.CARROT_ON_A_STICK) {
            player.getInventory().remove(player.getInventory().getItemInOffHand());
        }
    }

    private void removeTraderItem(Player player) {
        ItemStack customNetherStar = new ItemStack(Material.NETHER_STAR);
        ItemMeta meta = customNetherStar.getItemMeta();
        meta.setDisplayName("§9Heart Trader");
        customNetherStar.setItemMeta(meta);
        player.getInventory().remove(customNetherStar);
    }

    private String getRandomHeart() {
        Random random = new Random();
        int index = random.nextInt(heartTypes.size());
        return heartTypes.get(index);
    }

    public boolean verifyItem(PlayerInteractEvent event, String expectedName) {
        String displayName = event.getItem().getItemMeta().getDisplayName();
        return displayName != null && displayName.equals(expectedName);
    }

    private void overwritePlayerHeart(String playerName, String chosenHeart) {
        yamlReader.setValue("hearts.yml", playerName, chosenHeart.replace(" ", ""));
    }
}
