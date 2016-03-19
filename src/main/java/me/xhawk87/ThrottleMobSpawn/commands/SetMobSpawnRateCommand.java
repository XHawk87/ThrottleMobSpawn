package me.xhawk87.ThrottleMobSpawn.commands;

import me.xhawk87.ThrottleMobSpawn.ThrottleMobSpawn;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;

/**
 *
 * @author XHawk87
 */
public class SetMobSpawnRateCommand implements CommandExecutor {

    private ThrottleMobSpawn plugin;

    public SetMobSpawnRateCommand(ThrottleMobSpawn plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.hasPermission("throttlemobspawn.commands.setmobspawnrate")) {
            sender.sendMessage("Sorry, you don't have permission to use this command");
            return true;
        }

        if (args.length == 0) {
            sender.sendMessage("/SetMobSpawnRate [max spawns] [interval] - Sets the server-wide default spawn rate to a maximum number of spawns in a time interval in whole minutes");
            sender.sendMessage("/SetMobSpawnRate [spawn reason] [max spawns] [interval] - Sets the server-wide default spawn rate for a specific spawn reason to a maximum number of spawns in a time interval in whole minutes");
            sender.sendMessage("/SetMobSpawnRate [world] [max spawns] [interval] - Sets the default spawn rate for a given world to a maximum number of spawns in a time interval in whole minutes");
            sender.sendMessage("/SetMobSpawnRate [world] [spawn reason] [max spawns] [interval] - Sets the spawn rate for a specific spawn reason within a given world to a maximum number of spawns in a time interval in whole minutes");
            return true;
        }

        if (args.length < 2 && args.length > 4) {
            return false;
        }

        World world = null;
        SpawnReason spawnReason = null;
        if (args.length == 4) {
            world = plugin.getServer().getWorld(args[0]);
            if (world == null) {
                sender.sendMessage("World is not loaded: " + args[0]);
                return true;
            }
            try {
                spawnReason = SpawnReason.valueOf(args[1].toUpperCase());
            } catch (IllegalArgumentException ex) {
                sender.sendMessage(args[1] + " is not valid spawn reason");
                return true;
            }
        } else if (args.length == 3) {
            world = plugin.getServer().getWorld(args[0]);
            if (world == null) {
                try {
                    spawnReason = SpawnReason.valueOf(args[0].toUpperCase());
                } catch (IllegalArgumentException ex) {
                    sender.sendMessage(args[0] + " is not valid spawn reason nor a loaded world");
                    return true;
                }
            }
        }

        String spawnString = args[args.length - 2];
        String intervalString = args[args.length - 1];
        int spawn, interval;
        try {
            spawn = Integer.parseInt(spawnString);
        } catch (NumberFormatException ex) {
            sender.sendMessage("The number of spawns must be a whole number: " + spawnString);
            return true;
        }

        try {
            interval = Integer.parseInt(intervalString);
        } catch (NumberFormatException ex) {
            sender.sendMessage("The mob spawn interval must be a whole number of minutes: " + intervalString);
            return true;
        }

        if (world != null) {
            if (spawnReason != null) {
                plugin.setMobSpawnRate(world, spawnReason, spawn, interval);
                sender.sendMessage("The mob spawn rate for " + spawnReason.toString() + " within " + world.getName() + " has been set to " + spawn + " spawns in " + interval + " minutes");
            } else {
                plugin.setMobSpawnRate(world, spawn, interval);
                sender.sendMessage("The default mob spawn rate for " + world.getName() + " has been set to " + spawn + " spawns in " + interval + " minutes");
            }
        } else {
            if (spawnReason != null) {
                plugin.setMobSpawnRate(spawnReason, spawn, interval);
                sender.sendMessage("The default server-wide mob spawn rate for " + spawnReason.toString() + " has been set to " + spawn + " spawns in " + interval + " minutes");
            } else {
                plugin.setMobSpawnRate(spawn, interval);
                sender.sendMessage("The default server-wide mob spawn rate has been set to " + spawn + " spawns in " + interval + " minutes");
            }
        }
        return true;
    }
}
