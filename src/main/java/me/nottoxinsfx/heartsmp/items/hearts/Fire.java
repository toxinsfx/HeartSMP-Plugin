package me.nottoxinsfx.heartsmp.items.hearts;

import me.nottoxinsfx.heartsmp.Core;
import me.nottoxinsfx.heartsmp.utils.CoordinateUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.util.Vector;

import java.util.HashMap;
import java.util.Map;

public class Fire implements Listener {
    private final Map<Player, Long> cooldowns;

    public Fire() {
        this.cooldowns = new HashMap<>();
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();

        if (event.getItem() != null && event.getAction().isRightClick() && verifyHeart(event, "§6Fire Heart")) {
            if (player.isSneaking()) {

            } else {
                onRightClick(event);
            }
        }
    }

    public boolean verifyHeart(PlayerInteractEvent event, String expectedName) {
        String displayName = event.getItem().getItemMeta().getDisplayName();
        return displayName != null && displayName.equals(expectedName);
    }

    public void onRightClick(PlayerInteractEvent event) {
        if (verifyHeart(event, "§6Fire Heart")) {
            Player player = event.getPlayer();
            if (checkCooldown(player, "launchPlayer")) {
                long remainingTime = getRemainingCooldown(player, "Fire Heart");
                player.sendMessage(ChatColor.RED + "Your " + ChatColor.WHITE + "heart" + ChatColor.RED + " is on cooldown.");
                player.sendMessage(ChatColor.GRAY + "You must wait " + ChatColor.WHITE + remainingTime + ChatColor.GRAY + " seconds before using it again.");
                return;
            }
            applyCooldown(player, "launchPlayer");

            for (Player target : CoordinateUtils.getPlayersInRadius(player.getLocation(), 10)) {
                if (target instanceof Player && !target.equals(player)) {
                    Vector launchVector = new Vector(0, 1, 0).multiply(1.5);
                    target.setVelocity(launchVector);

                    target.damage(6);
                }
            }
        }
    }

    private boolean checkCooldown(Player player, String abilityName) {
        return cooldowns.containsKey(player) && cooldowns.get(player) > System.currentTimeMillis();
    }

    private long getRemainingCooldown(Player player, String abilityName) {
        if (cooldowns.containsKey(player)) {
            long remainingTime = cooldowns.get(player) - System.currentTimeMillis();
            return Math.max(0, remainingTime / 1000); // Convert milliseconds to seconds
        }
        return 0;
    }

    private void applyCooldown(Player player, String abilityName) {
        cooldowns.put(player, System.currentTimeMillis() + (75 * 1000)); // 75 seconds cooldown
    }
}
