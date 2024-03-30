package me.nottoxinsfx.heartsmp.items.hearts;

import me.nottoxinsfx.heartsmp.Core;
import me.nottoxinsfx.heartsmp.utils.CoordinateUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.HashMap;
import java.util.Map;

public class Life implements Listener {
    private final Map<Player, Map<String, Long>> cooldowns; // Map to track cooldowns for each player and ability

    public Life() {
        this.cooldowns = new HashMap<>();
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();

        if (event.getItem() != null && event.getAction().isRightClick() && verifyHeart(event,"§5Life Heart")) {
            if (player.isSneaking()) {
                onShiftRightClick(event);
            } else {
                onRightClick(event);
            }
        }
    }

    public boolean verifyHeart(PlayerInteractEvent event, String expectedName) {
        String displayName = event.getItem().getItemMeta().getDisplayName();
        return displayName != null && displayName.equals(expectedName);
    }

    public void onShiftRightClick(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if (checkCooldown(player, "removeHearts")) {
            long remainingTime = getRemainingCooldown(player, "removeHearts");
            player.sendMessage(ChatColor.RED + "Your " + ChatColor.WHITE + "ability" + ChatColor.RED + " is on cooldown.");
            player.sendMessage(ChatColor.GRAY + "You must wait " + ChatColor.WHITE + remainingTime + ChatColor.GRAY + " seconds before using it again.");
            return;
        }
        applyCooldown(player, "removeHearts");
        for (Player target : CoordinateUtils.getPlayersInRadius(player.getLocation(), 5)) {
            if (!target.equals(player)) {
                player.addPotionEffect(new PotionEffect(PotionEffectType.HUNGER, 30 * 20, 1));
                double currentHealth = target.getMaxHealth();
                double newHealth = Math.max(currentHealth - 8.0, 0.0);
                target.setMaxHealth(newHealth);
                Bukkit.getScheduler().runTaskLater(Core.INSTANCE, () -> target.resetMaxHealth(), 30 * 20); // 30 seconds duration
            }
        }
    }

    public void onRightClick(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if (checkCooldown(player, "addHearts")) {
            long remainingTime = getRemainingCooldown(player, "addHearts");
            player.sendMessage(ChatColor.RED + "Your " + ChatColor.WHITE + "ability" + ChatColor.RED + " is on cooldown.");
            player.sendMessage(ChatColor.GRAY + "You must wait " + ChatColor.WHITE + remainingTime + ChatColor.GRAY + " seconds before using it again.");
            return;
        }
        applyCooldown(player, "addHearts");

        player.addPotionEffect(new PotionEffect(PotionEffectType.SATURATION, 20 * 20, 1));
        double currentHealth = player.getMaxHealth();
        double newHealth = Math.min(currentHealth + 8.0, player.getMaxHealth());
        player.setMaxHealth(newHealth);
        Bukkit.getScheduler().runTaskLater(Core.INSTANCE, () -> player.resetMaxHealth(), 20 * 20); // 20 seconds duration
    }

    private boolean checkCooldown(Player player, String abilityName) {
        if (cooldowns.containsKey(player)) {
            Map<String, Long> playerCooldowns = cooldowns.get(player);
            if (playerCooldowns.containsKey(abilityName)) {
                long cooldownTime = playerCooldowns.get(abilityName);
                return cooldownTime > System.currentTimeMillis();
            }
        }
        return false;
    }

    private long getRemainingCooldown(Player player, String abilityName) {
        if (cooldowns.containsKey(player)) {
            Map<String, Long> playerCooldowns = cooldowns.get(player);
            if (playerCooldowns.containsKey(abilityName)) {
                long remainingTime = playerCooldowns.get(abilityName) - System.currentTimeMillis();
                return Math.max(0, remainingTime / 1000); // Convert milliseconds to seconds
            }
        }
        return 0;
    }

    private void applyCooldown(Player player, String abilityName) {
        cooldowns.putIfAbsent(player, new HashMap<>());
        Map<String, Long> playerCooldowns = cooldowns.get(player);
        playerCooldowns.put(abilityName, System.currentTimeMillis() + (75 * 1000)); // 75 seconds cooldown
    }
}
