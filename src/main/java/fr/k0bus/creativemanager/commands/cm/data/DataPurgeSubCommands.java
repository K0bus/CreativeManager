package fr.k0bus.creativemanager.commands.cm.data;

import fr.k0bus.creativemanager.CreativeManager;
import fr.k0bus.creativemanager.commands.SubCommands;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.conversations.Conversable;

public class DataPurgeSubCommands extends SubCommands {

    public DataPurgeSubCommands(CreativeManager instance) {
        super(instance, "creativemanager.admin", true);
    }

    @Override
    protected void run(CommandSender sender, String[] args) {
        if(sender instanceof Conversable)
        {
            sender.sendMessage("§§l§7CM DEBUG §8>>§r §6" + plugin.getDescription().getName() + " version §7:§b " + plugin.getDescription().getVersion());
            sender.sendMessage("§§l§7CM DEBUG §8>>§r §6Server version §7:§b " + Bukkit.getVersion());
            sender.sendMessage("§§l§7CM DEBUG §8>>§r §6MC version §7:§b " + Bukkit.getBukkitVersion());
        }
    }
}
