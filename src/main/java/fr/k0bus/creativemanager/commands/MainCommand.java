package fr.k0bus.creativemanager.commands;

import fr.k0bus.creativemanager.CreativeManager;
import fr.k0bus.creativemanager.gui.settings.ProtectionSettingGui;
import fr.k0bus.creativemanager.manager.InventoryManager;
import fr.k0bus.k0buslib.utils.Messages;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.conversations.Conversable;
import org.bukkit.entity.Player;

/**
 * Main command class.
 */
public class MainCommand implements CommandExecutor {
    private final CreativeManager plugin;

    /**
     * Instantiates a new Main command.
     *
     * @param instance the instance.
     */
    public MainCommand(CreativeManager instance) {
        plugin = instance;
    }

    /**
     * On command.
     *
     * @param sender the sender.
     * @param cmd    the cmd.
     * @param label  the label.
     * @param args   the args.
     * @return the boolean.
     */
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        Conversable conversable = null;
        if(sender instanceof ConsoleCommandSender)
            conversable = (ConsoleCommandSender) sender;
        else if(sender instanceof Player)
            conversable = (Player) sender;
        if (args.length >= 1) {
            if (args[0].equals("reload")) {
                if (!(sender instanceof Player) || sender.hasPermission("creativemanager.reload")) {
                    plugin.loadConfigManager();
                    Messages.sendMessageText(plugin.getMessageManager(), conversable, " &5Configuration reloaded !");
                } else {
                    Messages.sendMessage(plugin.getMessageManager(), conversable, "permission.general");
                }
            } else if (args[0].equals("settings")) {
                if (sender instanceof Player && sender.hasPermission("creativemanager.admin")) {
                    new ProtectionSettingGui((Player) sender, plugin).show();
                } else {
                    Messages.sendMessage(plugin.getMessageManager(), conversable, "permission.general");
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
                    Messages.sendMessageText(plugin.getMessageManager(), conversable, " &4Missing argument !");
                }
            } else {
                Messages.sendMessageText(plugin.getMessageManager(), conversable, " &4Unknown command " + args[0] + " !");
            }
        } else {
            Messages.sendMessageText(plugin.getMessageManager(), conversable, " &aRunning " + plugin.getName() + " v" + plugin.getDescription().getVersion());
        }
        return true;
    }
}