/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package me.xhawk87.ThrottleMobSpawn;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;
import org.bukkit.World;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;

/**
 *
 * @author XHawk87
 */
public class ChunkSpawnMonitor {

    private EnumSet<SpawnReason> enabledByDefault = EnumSet.of(
            SpawnReason.BREEDING,
            SpawnReason.NATURAL,
            SpawnReason.SPAWNER);
    private Map<SpawnReason, SpawnMonitor> byReason = new HashMap<>();

    public ChunkSpawnMonitor(ThrottleMobSpawn plugin, World world) {
        for (SpawnReason reason : SpawnReason.values()) {
            int maxSpawns = plugin.getSpawns(world, reason);
            if (maxSpawns == ThrottleMobSpawn.NOT_ASSIGNED) {
                maxSpawns = plugin.getSpawns(reason);
                if (maxSpawns == ThrottleMobSpawn.NOT_ASSIGNED) {
                    if (enabledByDefault.contains(reason)) {
                        maxSpawns = plugin.getSpawns();
                    } else {
                        continue;
                    }
                }
            }
            int interval = plugin.getInterval(world, reason);
            if (interval == ThrottleMobSpawn.NOT_ASSIGNED) {
                interval = plugin.getInterval(reason);
                if (interval == ThrottleMobSpawn.NOT_ASSIGNED) {
                    interval = plugin.getInterval();
                }
            }
            byReason.put(reason, new SpawnMonitor(maxSpawns, interval));
        }
    }

    public boolean canSpawn(LivingEntity entity, SpawnReason reason) {
        if (byReason.containsKey(reason)) {
            SpawnMonitor reasonMonitor = byReason.get(reason);
            if (reasonMonitor.canSpawn()) {
                reasonMonitor.spawn();
                return true;
            } else {
                return false;
            }
        } else {
            return true;
        }
    }

    public void showDebug(Player player) {
        for (Map.Entry<SpawnReason, SpawnMonitor> entry : byReason.entrySet()) {
            SpawnReason spawnReason = entry.getKey();
            SpawnMonitor spawnMonitor = entry.getValue();
            if (spawnMonitor.getMaxSpawns() != ThrottleMobSpawn.UNLIMITED) {
                player.sendMessage(spawnReason.name().toLowerCase() + ": " + spawnMonitor.toString());
            }
        }
    }
}
