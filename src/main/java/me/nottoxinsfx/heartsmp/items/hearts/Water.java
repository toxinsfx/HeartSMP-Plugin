package me.nottoxinsfx.heartsmp.items.hearts;

import me.nottoxinsfx.heartsmp.Core;
import me.nottoxinsfx.heartsmp.utils.CoordinateUtils;
import me.nottoxinsfx.heartsmp.utils.ParticleUtils;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.HashMap;
import java.util.Map;

public class Water implements Listener {
    private final Map<String, Long> cooldowns;

    public Water() {
        this.cooldowns = new HashMap<>();
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if (event.getItem() != null && event.getAction().isRightClick() && verifyHeart(event, "§9Water Heart")) {
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
        if (verifyHeart(event, "§9Water Heart")) {
            if (checkCooldown("Water Heart")) {
                long remainingTime = getRemainingCooldown("Water Heart");
                player.sendMessage(ChatColor.RED + "The " + ChatColor.WHITE + "Water Heart" + ChatColor.RED + " is on cooldown.");
                player.sendMessage(ChatColor.GRAY + "You must wait " + ChatColor.WHITE + remainingTime + ChatColor.GRAY + " seconds before using it again.");
                return;
            }
            applyCooldown("Water Heart");
            Location playerLocation = player.getLocation();
            for (Player target : CoordinateUtils.getPlayersInRadius(playerLocation, 5)) {
                target.addPotionEffect(new PotionEffect(PotionEffectType.WATER_BREATHING, (30 * 20), 1));
                target.addPotionEffect(new PotionEffect(PotionEffectType.DOLPHINS_GRACE, (30 * 20), 2));
            }
        }
    }

    public void onRightClick(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if (verifyHeart(event, "§9Water Heart")) {
            if (checkCooldown("Water Heartt")) {
                long remainingTime = getRemainingCooldown("Water Heartt");
                player.sendMessage(ChatColor.RED + "The " + ChatColor.WHITE + "Water Heart" + ChatColor.RED + " is on cooldown.");
                player.sendMessage(ChatColor.GRAY + "You must wait " + ChatColor.WHITE + remainingTime + ChatColor.GRAY + " seconds before using it again.");
                return;
            }
            applyCooldown("Water Heartt");

            Location playerLocation = player.getLocation();
            for (Player target : CoordinateUtils.getPlayersInRadius(playerLocation, 20)) {
                if (!target.equals(player)) {
                    Core.INSTANCE.getLogger().info(target.getName());
                    Location sourceLocation = player.getEyeLocation().add(0, -1, 0);
                    Location targetLocation = target.getEyeLocation().add(0, -1, 0);
                    ParticleUtils.spawnParticlesBetween(sourceLocation, targetLocation, Particle.WATER_SPLASH, 100);

                    target.damage(6);
                    break;
                }
            }
        }
    }

    private boolean checkCooldown(String abilityName) {
        return cooldowns.containsKey(abilityName) && cooldowns.get(abilityName) > System.currentTimeMillis();
    }

    private long getRemainingCooldown(String abilityName) {
        if (cooldowns.containsKey(abilityName)) {
            long remainingTime = cooldowns.get(abilityName) - System.currentTimeMillis();
            return Math.max(0, remainingTime / 1000); // Convert milliseconds to seconds
        }
        return 0;
    }

    private void applyCooldown(String abilityName) {
        cooldowns.put(abilityName, System.currentTimeMillis() + (75 * 1000)); // 75 seconds cooldown
    }
}
