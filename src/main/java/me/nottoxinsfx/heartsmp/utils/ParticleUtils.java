package me.nottoxinsfx.heartsmp.utils;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.World;

public class ParticleUtils {
    /**
     * Spawns particles at the specified location with default offset and extra values.
     *
     * @param location  The location where particles should be spawned.
     * @param particle  The type of particle to spawn.
     * @param count     The number of particles to spawn.
     */
    public static void spawnParticles(Location location, Particle particle, int count) {
        World world = location.getWorld();
        if (world != null) {
            world.spawnParticle(particle, location, count, 0, 0, 0, 0);
        }
    }

    /**
     * Spawns particles between two locations.
     *
     * @param source     The source location.
     * @param target     The target location.
     * @param particle   The type of particle to spawn.
     * @param count      The number of particles to spawn.
     */
    public static void spawnParticlesBetween(Location source, Location target, Particle particle, int count) {
        World world = source.getWorld();
        if (world != null) {
            double distance = source.distance(target);
            double increment = 0.1;

            for (double d = 0; d <= distance; d += increment) {
                Location particleLocation = source.clone().add(target.clone().subtract(source).toVector().normalize().multiply(d));
                world.spawnParticle(particle, particleLocation, count);
            }
        }
    }
}
