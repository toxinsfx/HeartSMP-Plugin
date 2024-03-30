package me.nottoxinsfx.heartsmp.items.hearts;

import me.nottoxinsfx.heartsmp.Core;
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

public class Strength implements Listener {

    private final Map<Player, Map<String, Long>> cooldowns;

    public Strength() {
        this.cooldowns = new HashMap<>();
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();

        if (event.getItem() != null && event.getAction().isRightClick() && verifyHeart(event, "§cStrength Heart")) {
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
        String heartName = ChatColor.stripColor(event.getItem().getItemMeta().getDisplayName());

        if (verifyHeart(event, "§cStrength Heart")) {
            if (checkCooldown(player, "ST")) {
                long remainingTime = getRemainingCooldown(player, "ST");
                player.sendMessage(ChatColor.RED + "Your " + ChatColor.WHITE + "heart" + ChatColor.RED + " is on cooldown.");
                player.sendMessage(ChatColor.GRAY + "You must wait " + ChatColor.WHITE + remainingTime + ChatColor.GRAY + " seconds before using it again.");
                return;
            }
            applyCooldown(player, "ST");
            player.setInvulnerable(true);
            Bukkit.getServer().getScheduler().runTaskLater(Core.INSTANCE, () -> player.setInvulnerable(false), 30 * 20L); // 30 seconds duration
        }
    }

    public void onRightClick(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        String heartName = ChatColor.stripColor(event.getItem().getItemMeta().getDisplayName());

        if (verifyHeart(event, "§cStrength Heart")) {
            if (checkCooldown(player, "st2")) {
                long remainingTime = getRemainingCooldown(player, "st2");
                player.sendMessage(ChatColor.RED + "Your " + ChatColor.WHITE + "heart" + ChatColor.RED + " is on cooldown.");
                player.sendMessage(ChatColor.GRAY + "You must wait " + ChatColor.WHITE + remainingTime + ChatColor.GRAY + " seconds before using it again.");
                return;
            }
            applyCooldown(player, "st2");
            player.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 200, 2)); // 10 seconds duration
        }
    }

    private boolean checkCooldown(Player player, String heartName) {
        if (cooldowns.containsKey(player)) {
            Map<String, Long> playerCooldowns = cooldowns.get(player);
            if (playerCooldowns.containsKey(heartName)) {
                long cooldownTime = playerCooldowns.get(heartName);
                return cooldownTime > System.currentTimeMillis();
            }
        }
        return false;
    }

    private long getRemainingCooldown(Player player, String heartName) {
        if (cooldowns.containsKey(player)) {
            Map<String, Long> playerCooldowns = cooldowns.get(player);
            if (playerCooldowns.containsKey(heartName)) {
                long cooldownEnd = playerCooldowns.get(heartName);
                return Math.max(0, (cooldownEnd - System.currentTimeMillis()) / 1000); // Convert milliseconds to seconds
            }
        }
        return 0;
    }

    private void applyCooldown(Player player, String heartName) {
        long cooldownTime = System.currentTimeMillis() + (75 * 1000); // 75 seconds cooldown
        if (!cooldowns.containsKey(player)) {
            cooldowns.put(player, new HashMap<>());
        }
        cooldowns.get(player).put(heartName, cooldownTime);
    }
}
