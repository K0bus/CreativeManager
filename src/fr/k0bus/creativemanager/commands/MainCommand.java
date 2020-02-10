package fr.k0bus.creativemanager.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import fr.k0bus.creativemanager.Main;

public class MainCommand implements CommandExecutor {

    Main plugin;

    public MainCommand(Main instance)
    {
        plugin = instance;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if(args.length>=1)
        {
            if(args[0].equals("reload"))
            {
                if(sender instanceof Player == false || sender.hasPermission("creativemanager.reload"))
                {
                    plugin.loadConfigManager();
                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("tag") + " &5Configuration reloaded !"));
                }
                else
                {
                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("tag") + plugin.getConfig().getString("lang.permission.general")));
                }
            }
            else
            {
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("tag") + " &4Unknown command " + args[0] + " !"));
            }
        }
        else
        {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("tag") + " &aRunning CreativeManager v0.1"));
        }
        
        return true;
    }
}