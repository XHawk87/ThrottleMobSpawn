package me.xhawk87.ThrottleMobSpawn.utils;

import java.util.Objects;
import java.util.UUID;
import org.bukkit.Chunk;
import org.bukkit.Server;

/**
 * ChunkLocation
 *
 * @author XHawk87
 */
public class ChunkLocation {
    
    private UUID world;
    private int x;
    private int z;
    
    public ChunkLocation(Chunk chunk) {
        this.world = chunk.getWorld().getUID();
        this.x = chunk.getX();
        this.z = chunk.getZ();
    }

    public UUID getWorld() {
        return world;
    }
    
    public boolean isLoaded(Server server) {
        return server.getWorld(world).isChunkLoaded(x, z);
    }
    
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof ChunkLocation) {
            ChunkLocation other = (ChunkLocation) obj;
            return other.x == x && other.z == z && other.world.equals(world);
        }
        return false;
    }
    
    @Override
    public int hashCode() {
        int hash = 7;
        hash = 17 * hash + Objects.hashCode(this.world);
        hash = 17 * hash + this.x;
        hash = 17 * hash + this.z;
        return hash;
    }
}
