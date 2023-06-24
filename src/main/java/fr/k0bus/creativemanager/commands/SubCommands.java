package fr.k0bus.creativemanager.commands;

import fr.k0bus.creativemanager.CreativeManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.conversations.Conversable;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.HashMap;

public abstract class SubCommands implements CommandExecutor {

    public final String permissions;
    public final boolean playerOnly;
    public final CreativeManager plugin;
    private final HashMap<String, SubCommands> subCommands = new HashMap<>();
    private String defaultSubCmd = null;

    public SubCommands(CreativeManager instance)
    {
        this.plugin = instance;
        this.permissions = null;
        this.playerOnly = false;
    }
    public SubCommands(CreativeManager instance, String perms)
    {
        this.plugin = instance;
        this.permissions = perms;
        this.playerOnly = false;
    }
    public SubCommands(CreativeManager instance, boolean playerOnly)
    {
        this.plugin = instance;
        this.permissions = null;
        this.playerOnly = playerOnly;
    }
    public SubCommands(CreativeManager instance, String perms, boolean playerOnly)
    {
        this.plugin = instance;
        this.permissions = perms;
        this.playerOnly = playerOnly;
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

    protected void run(CommandSender sender, String[] args){}

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if(subCmd(sender, args)) return true;
        if(cantUse(sender)) return true;
        run(sender, args);
        return true;
    }

    public void onCommand(CommandSender sender, String[] args) {
        if(subCmd(sender, args)) return;
        if(cantUse(sender)) return;
        run(sender, args);
    }

    public boolean canUse(CommandSender sender, boolean sendMessage)
    {
        if(checkPlayer(sender, sendMessage))
            return checkPermission(sender, sendMessage);
        return false;
    }

    public boolean cantUse(CommandSender sender)
    {
        return !canUse(sender, true);
    }
    private boolean checkPermission(CommandSender sender, boolean sendMessage)
    {
        if(permissions != null)
            if(!sender.hasPermission(permissions))
            {
                if(sendMessage)
                    sender.sendMessage(CreativeManager.TAG + CreativeManager.getLang().getString("permission.general"));
                return false;
            }
        return true;
    }
    private boolean checkPlayer(CommandSender sender, boolean sendMessage)
    {
        if(playerOnly)
            if(!(sender instanceof Player))
            {
                if(sendMessage)
                    sender.sendMessage(CreativeManager.TAG + CreativeManager.getLang().getString("permission.general"));
                return false;
            }
        return true;
    }
    private boolean subCmd(CommandSender sender, String[] args)
    {
        if(subCommands.size() == 0) return false;
        if(args.length > 0) {
            if (subCommands.containsKey(args[0]))
            {
                subCommands.get(args[0]).onCommand(sender, Arrays.copyOfRange(args, 1, args.length));
                return true;
            }
            else if (sender instanceof Conversable)
                sender.sendMessage(CreativeManager.TAG + CreativeManager.getSettings().getTag() + " &cUnknown subcommands !");
        }else {
            if (defaultSubCmd != null)
                subCommands.get(defaultSubCmd).onCommand(sender, args);
            else
                    sender.sendMessage(CreativeManager.TAG + CreativeManager.getSettings().getTag() + " &cMissing arguments !");
        }

        return false;
    }

    public HashMap<String, SubCommands> getSubCommands() {
        return subCommands;
    }

    public CreativeManager getPlugin() {
        return plugin;
    }

}
