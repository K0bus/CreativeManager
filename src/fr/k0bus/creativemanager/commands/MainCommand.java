package fr.k0bus.creativemanager.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import fr.k0bus.creativemanager.Main;
import fr.k0bus.creativemanager.manager.InventoryManager;

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
                if(!(sender instanceof Player) || sender.hasPermission("creativemanager.reload"))
                {
                    plugin.loadConfigManager();
                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("tag") + " &5Configuration reloaded !"));
                }
                else
                {
                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("tag") + plugin.getLang().getString("permission.general")));
                }
            }
            else if(args[0].equals("inventory") && sender instanceof Player && sender.hasPermission("creativemanager.admin"))
            {
                if(args.length>=2)
                {
                    Player p = (Player) sender;
                    InventoryManager im = new InventoryManager(p, plugin);
                    switch (args[1]) {
                        case "save":
                            im.saveInventory(p.getGameMode());
                            break;
                        case "load":
                            im.loadInventory(p.getGameMode());
                            break;
                        default:
                            break;
                    }
                }
                else
                {
                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("tag") + " &4Missing argument !"));
                }
            }
            else
            {
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("tag") + " &4Unknown command " + args[0] + " !"));
            }
        }
        else
        {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("tag") + " &aRunning " + plugin.getName() + " v" + plugin.getDescription().getVersion()));
        }
        
        return true;
    }
}