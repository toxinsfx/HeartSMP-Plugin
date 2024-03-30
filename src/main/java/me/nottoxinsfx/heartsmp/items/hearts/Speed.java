package me.nottoxinsfx.heartsmp.items.hearts;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.HashMap;
import java.util.Map;

public class Speed implements Listener {

    private final Map<Player, Map<String, Long>> cooldowns;

    public Speed() {
        this.cooldowns = new HashMap<>();
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();

        if (event.getItem() != null && event.getAction().isRightClick() && verifyHeart(event, "§eSpeed Heart")) {
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
        if (checkCooldown(player, "SpeedHeartShift")) {
            long remainingTime = getRemainingCooldown(player, "SpeedHeartShift");
            player.sendMessage(ChatColor.RED + "Your " + ChatColor.WHITE + "ability" + ChatColor.RED + " is on cooldown.");
            player.sendMessage(ChatColor.GRAY + "You must wait " + ChatColor.WHITE + remainingTime + ChatColor.GRAY + " seconds before using it again.");
            return;
        }
        applyCooldown(player, "SpeedHeartShift");
        player.addPotionEffect(new PotionEffect(PotionEffectType.FAST_DIGGING, 600, 254)); // 30 seconds duration
    }

    public void onRightClick(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if (checkCooldown(player, "SpeedHeart")) {
            long remainingTime = getRemainingCooldown(player, "SpeedHeart");
            player.sendMessage(ChatColor.RED + "Your " + ChatColor.WHITE + "ability" + ChatColor.RED + " is on cooldown.");
            player.sendMessage(ChatColor.GRAY + "You must wait " + ChatColor.WHITE + remainingTime + ChatColor.GRAY + " seconds before using it again.");
            return;
        }
        applyCooldown(player, "SpeedHeart");
        player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 400, 2)); // 20 seconds duration
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
        playerCooldowns.put(abilityName, System.currentTimeMillis() + (75 * 1000)); // 30 seconds cooldown
    }
}
