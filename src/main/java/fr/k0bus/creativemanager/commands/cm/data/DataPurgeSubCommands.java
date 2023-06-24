package fr.k0bus.creativemanager.commands.cm.data;

import fr.k0bus.creativemanager.CreativeManager;
import fr.k0bus.creativemanager.commands.SubCommands;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

public class DataPurgeSubCommands extends SubCommands {

    public DataPurgeSubCommands(CreativeManager instance) {
        super(instance, "creativemanager.admin", true);
    }

    @Override
    protected void run(CommandSender sender, String[] args) {
        sender.sendMessage(CreativeManager.TAG + ChatColor.RED + "Not available yet.");
    }
}
