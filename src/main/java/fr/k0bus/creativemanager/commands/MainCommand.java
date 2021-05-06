package fr.k0bus.creativemanager.commands;

import fr.k0bus.creativemanager.gui.settings.ProtectionSettingGui;
import fr.k0bus.creativemanager.utils.Messages;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import fr.k0bus.creativemanager.CreativeManager;
import fr.k0bus.creativemanager.manager.InventoryManager;

public class MainCommand implements CommandExecutor {

    CreativeManager plugin;

    public MainCommand(CreativeManager instance)
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
                    Messages.sendMessageText(plugin, sender, " &5Configuration reloaded !");
                }
                else
                {
                    Messages.sendMessage(plugin, sender, "permission.general");
                }
            } else if (args[0].equals("settings")) {
                if(sender instanceof Player && sender.hasPermission("creativemanager.admin"))
                {
                    new ProtectionSettingGui((Player) sender, plugin).show();
                }
                else
                {
                    Messages.sendMessage(plugin, sender, "permission.general");
                }
            } else if (args[0].equals("inventory") && sender instanceof Player && sender.hasPermission("creativemanager.admin")) {
                if (args.length >= 2) {
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
                } else {
                    Messages.sendMessageText(plugin, sender, " &4Missing argument !");
                }
            } else {
                Messages.sendMessageText(plugin, sender, " &4Unknown command " + args[0] + " !");
            }
        }
        else
        {
            Messages.sendMessageText(plugin, sender, " &aRunning " + plugin.getName() + " v" + plugin.getDescription().getVersion());
        }
        
        return true;
    }
}