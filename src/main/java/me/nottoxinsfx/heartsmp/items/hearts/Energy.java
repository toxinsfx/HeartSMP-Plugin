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
import org.bukkit.util.Vector;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Energy implements Listener {
    private final Map<Player, Map<String, Long>> cooldowns; // Map to track cooldowns for each player and ability

    public Energy() {
        this.cooldowns = new HashMap<>();
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();

        if (event.getItem() != null && event.getAction().isRightClick() && verifyHeart(event, "§aEnergy Heart")) {
            if (player.isSneaking()) {
                onShiftRightClick(event);
            }
        }
    }

    public boolean verifyHeart(PlayerInteractEvent event, String expectedName) {
        String displayName = event.getItem().getItemMeta().getDisplayName();
        return displayName != null && displayName.equals(expectedName);
    }

    public void onShiftRightClick(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if (checkCooldown(player, "SonicBoom")) {
            long remainingTime = getRemainingCooldown(player, "SonicBoom");
            player.sendMessage(ChatColor.RED + "Your " + ChatColor.WHITE + "ability" + ChatColor.RED + " is on cooldown.");
            player.sendMessage(ChatColor.GRAY + "You must wait " + ChatColor.WHITE + remainingTime + ChatColor.GRAY + " seconds before using it again.");
            return;
        }
        applyCooldown(player, "SonicBoom");


        Location playerLocation = player.getLocation();

        Vector direction = player.getEyeLocation().getDirection().normalize();
        double distance = 15;
        Location targetLocation = player.getLocation().add(direction.multiply(distance));
        ParticleUtils.spawnParticlesBetween(player.getEyeLocation(), targetLocation, Particle.SONIC_BOOM, 100);

        List<Player> nearbyPlayers = CoordinateUtils.getPlayersInRadius(playerLocation, 20);
        for (Player target : nearbyPlayers) {
            if (!target.equals(player)) {
                target.damage(8);
                break;
            }
        }
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
