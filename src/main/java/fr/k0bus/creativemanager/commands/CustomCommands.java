package fr.k0bus.creativemanager.commands;

import fr.k0bus.creativemanager.CreativeManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.conversations.Conversable;

import java.util.Arrays;
import java.util.HashMap;

public abstract class CustomCommands implements CommandExecutor {
    private final CreativeManager plugin;
    private final HashMap<String, SubCommands> subCommands = new HashMap<>();
    private String defaultSubCmd = null;
    public CustomCommands(CreativeManager instance)
    {
        plugin = instance;
    }
    public void registerCommands(String cmd, SubCommands commands)
    {
        if(cmd != null && commands != null)
            subCommands.put(cmd, commands);
    }
    public void setDefaultSubCmd(String cmd)
    {
        if(subCommands.containsKey(cmd))
            this.defaultSubCmd = cmd;
    }

    public CreativeManager getPlugin() {
        return plugin;
    }

    public HashMap<String, SubCommands> getSubCommands() {
        return subCommands;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if(args.length > 0) {
            if (subCommands.containsKey(args[0]))
                subCommands.get(args[0]).onCommand(sender, Arrays.copyOfRange(args, 1, args.length));
            else if (sender instanceof Conversable)
                sender.sendMessage(CreativeManager.TAG + "&cUnknown subcommands !");
        }else {
            if (defaultSubCmd != null)
                subCommands.get(defaultSubCmd).onCommand(sender, args);
            else
                sender.sendMessage(CreativeManager.TAG + " &cMissing arguments !");
        }

        return true;
    }
}
