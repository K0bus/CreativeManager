package fr.k0bus.creativemanager.commands.cm;

import fr.k0bus.creativemanager.CreativeManager;
import fr.k0bus.creativemanager.commands.Commands;
import fr.k0bus.k0buscore.utils.StringUtils;
import org.bukkit.command.CommandSender;

public class ReloadSubCommands extends Commands {

    public ReloadSubCommands(CreativeManager instance) {
        super(instance, "creativemanager.admin", false);
    }

    @Override
    protected void run(CommandSender sender, String[] args) {
        plugin.loadConfigManager();
        sender.sendMessage(CreativeManager.TAG + StringUtils.translateColor("&5Configuration reloaded !"));
    }
}
