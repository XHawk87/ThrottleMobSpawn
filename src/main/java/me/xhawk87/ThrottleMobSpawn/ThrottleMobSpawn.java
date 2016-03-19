package me.xhawk87.ThrottleMobSpawn;

import me.xhawk87.ThrottleMobSpawn.commands.SetMobSpawnRateCommand;
import me.xhawk87.ThrottleMobSpawn.listeners.SpawnListener;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import me.xhawk87.ThrottleMobSpawn.commands.TMSDebugCommand;
import org.bukkit.World;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;
import org.bukkit.plugin.java.JavaPlugin;

/**
 *
 * @author XHawk87
 */
public class ThrottleMobSpawn extends JavaPlugin {
    
    public static final int UNLIMITED = -1;
    public static final int NOT_ASSIGNED = -2;
    private Map<UUID, WorldInfo> worlds = new HashMap<>();
    
    @Override
    public void onEnable() {
        saveDefaultConfig();

        // Register listeners
        new SpawnListener().registerEvents(this);

        // Register commands
        getCommand("SetMobSpawnRate").setExecutor(new SetMobSpawnRateCommand(this));
        getCommand("TMSDebug").setExecutor(new TMSDebugCommand(this));
    }
    
    public WorldInfo getWorldInfo(World world) {
        WorldInfo info = worlds.get(world.getUID());
        if (info == null) {
            info = new WorldInfo(this);
            worlds.put(world.getUID(), info);
        }
        return info;
    }
    
    public int getSpawns() {
        return getConfig().getInt("spawns", 16);
    }
    
    public int getInterval() {
        return getConfig().getInt("interval", 60);
    }
    
    public int getSpawns(SpawnReason reason) {
        return getConfig().getInt(reason.name().toLowerCase() + ".spawns", NOT_ASSIGNED);
    }
    
    public int getInterval(SpawnReason reason) {
        return getConfig().getInt(reason.name().toLowerCase() + ".interval", NOT_ASSIGNED);
    }
    
    public int getSpawns(World world) {
        return getConfig().getInt("worlds." + world.getName().toLowerCase() + ".spawns", NOT_ASSIGNED);
    }
    
    public int getInterval(World world) {
        return getConfig().getInt("worlds." + world.getName().toLowerCase() + ".interval", NOT_ASSIGNED);
    }
    
    public int getSpawns(World world, SpawnReason reason) {
        return getConfig().getInt("worlds." + world.getName().toLowerCase() + "." + reason.name().toLowerCase() + ".spawns", NOT_ASSIGNED);
    }
    
    public int getInterval(World world, SpawnReason reason) {
        return getConfig().getInt("worlds." + world.getName().toLowerCase() + "." + reason.name().toLowerCase() + ".interval", NOT_ASSIGNED);
    }
    
    public void setMobSpawnRate(int spawns, int interval) {
        getConfig().set("spawns", spawns);
        getConfig().set("interval", interval);
        saveConfig();
        for (WorldInfo worldInfo : worlds.values()) {
            worldInfo.flushCache();
        }
    }
    
    public void setMobSpawnRate(SpawnReason reason, int spawns, int interval) {
        String reasonName = reason.name().toLowerCase();
        getConfig().set(reasonName + ".spawns", spawns);
        getConfig().set(reasonName + ".interval", interval);
        saveConfig();
        for (WorldInfo worldInfo : worlds.values()) {
            worldInfo.flushCache();
        }
    }
    
    public void setMobSpawnRate(World world, int spawns, int interval) {
        getConfig().set("worlds." + world.getName() + ".spawns", spawns);
        getConfig().set("worlds." + world.getName() + ".interval", interval);
        saveConfig();
        getWorldInfo(world).flushCache();
    }
    
    public void setMobSpawnRate(World world, SpawnReason reason, int spawns, int interval) {
        String reasonName = reason.name().toLowerCase();
        getConfig().set("worlds." + world.getName() + "." + reasonName + ".spawns", spawns);
        getConfig().set("worlds." + world.getName() + "." + reasonName + ".interval", interval);
        saveConfig();
        getWorldInfo(world).flushCache();
    }
    
    public boolean canSpawn(LivingEntity entity, SpawnReason spawnReason) {
        return getWorldInfo(entity.getWorld()).canSpawn(entity, spawnReason);
    }
}
