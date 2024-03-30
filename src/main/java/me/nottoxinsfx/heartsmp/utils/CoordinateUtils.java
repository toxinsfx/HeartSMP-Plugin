package me.nottoxinsfx.heartsmp.utils;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;

/**
 * A utility class for coordinate-related operations in Bukkit plugins.
 */
public class CoordinateUtils {

    /**
     * Checks if a player is looking at another player.
     *
     * @param player The player who is looking.
     * @param target The player who is being looked at.
     * @return {@code true} if the player is looking at the target, {@code false} otherwise.
     */
    public static boolean isLookingAt(Player player, Player target) {
        Vector playerDirection = player.getEyeLocation().getDirection();
        Vector targetDirection = target.getLocation().subtract(player.getEyeLocation()).toVector().normalize();
        double dotProduct = playerDirection.dot(targetDirection);
        return dotProduct > 0.99;
    }

    /**
     * Retrieves all players within a specified radius around a location.
     *
     * @param location The center location.
     * @param radius   The radius around the location.
     * @return A list of players within the specified radius of the location.
     */
    public static List<Player> getPlayersInRadius(Location location, double radius) {
        List<Player> playersInRadius = new ArrayList<>();
        for (Player player : location.getWorld().getPlayers()) {
            if (player.getLocation().distanceSquared(location) <= radius * radius) {
                playersInRadius.add(player);
            }
        }
        return playersInRadius;
    }
}
