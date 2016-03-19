package me.xhawk87.ThrottleMobSpawn;

import me.xhawk87.ThrottleMobSpawn.utils.ChunkLocation;
import java.util.HashMap;
import java.util.Map;
import org.bukkit.Chunk;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;

/**
 * WorldInfo
 *
 * @author XHawk87
 */
public class WorldInfo {

    private Map<ChunkLocation, ChunkSpawnMonitor> worldSpawnMonitors = new HashMap<>();
    private ThrottleMobSpawn plugin;

    public WorldInfo(ThrottleMobSpawn plugin) {
        this.plugin = plugin;
    }

    private ChunkSpawnMonitor getChunkSpawnMonitor(Chunk chunk) {
        ChunkLocation chunkLocation = new ChunkLocation(chunk);
        ChunkSpawnMonitor chunkSpawnMonitor = worldSpawnMonitors.get(chunkLocation);
        if (chunkSpawnMonitor == null) {
            chunkSpawnMonitor = new ChunkSpawnMonitor(plugin, chunk.getWorld());
            worldSpawnMonitors.put(chunkLocation, chunkSpawnMonitor);
        }
        return chunkSpawnMonitor;
    }

    public boolean canSpawn(LivingEntity entity, SpawnReason spawnReason) {
        return getChunkSpawnMonitor(entity.getLocation().getChunk())
                .canSpawn(entity, spawnReason);
    }

    public void flushCache() {
        worldSpawnMonitors.clear();
    }

    public void showDebug(Player player) {
        getChunkSpawnMonitor(player.getLocation().getChunk()).showDebug(player);
    }
}
