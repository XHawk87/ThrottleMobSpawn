package me.xhawk87.ThrottleMobSpawn.listeners;

import me.xhawk87.ThrottleMobSpawn.ThrottleMobSpawn;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;

/**
 * SpawnListener
 *
 * @author XHawk87
 */
public class SpawnListener implements Listener {

    private ThrottleMobSpawn plugin;

    public void registerEvents(ThrottleMobSpawn plugin) {
        this.plugin = plugin;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void onMobSpawned(CreatureSpawnEvent event) {
        if (!plugin.canSpawn(event.getEntity(), event.getSpawnReason())) {
            event.setCancelled(true);
        }
    }
}
