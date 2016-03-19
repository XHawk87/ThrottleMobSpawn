package me.xhawk87.ThrottleMobSpawn.commands;

import me.xhawk87.ThrottleMobSpawn.ThrottleMobSpawn;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 *
 * @author XHawk87
 */
public class TMSDebugCommand implements CommandExecutor {

    private ThrottleMobSpawn plugin;

    public TMSDebugCommand(ThrottleMobSpawn plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.hasPermission("throttlemobspawn.commands.tmsdebug")) {
            sender.sendMessage("You do not have permission to use this command");
            return true;
        }
        
        if (sender instanceof Player) {
            Player player = (Player) sender;
            plugin.getWorldInfo(player.getWorld()).showDebug(player);
            return true;
        } else {
            sender.sendMessage("You must be signed in to use this command");
            return true;
        }
    }
}
