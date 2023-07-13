package fr.k0bus.creativemanager.commands.cm;

import fr.k0bus.creativemanager.CreativeManager;
import fr.k0bus.creativemanager.commands.Commands;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.conversations.Conversable;

public class InfosSubCommands extends Commands {

    public InfosSubCommands(CreativeManager instance) {
        super(instance, "creativemanager.admin", true);
    }

    @Override
    protected void run(CommandSender sender, String[] args) {
        if(sender instanceof Conversable)
        {
            sender.sendMessage(CreativeManager.TAG + "§r");
            sender.sendMessage(CreativeManager.TAG + "§§l§7CM DEBUG §8>>§r §6Send me this when asking me some help");
            sender.sendMessage(CreativeManager.TAG + "§r");
            sender.sendMessage(CreativeManager.TAG + "§§l§7CM DEBUG §8>>§r §6" + plugin.getDescription().getName() + " version §7:§b " + plugin.getDescription().getVersion());
            sender.sendMessage(CreativeManager.TAG + "§§l§7CM DEBUG §8>>§r §6Server version §7:§b " + Bukkit.getVersion());
            sender.sendMessage(CreativeManager.TAG + "§§l§7CM DEBUG §8>>§r §6MC version §7:§b " + Bukkit.getBukkitVersion());
            sender.sendMessage(CreativeManager.TAG + "§r");
        }
    }
}
