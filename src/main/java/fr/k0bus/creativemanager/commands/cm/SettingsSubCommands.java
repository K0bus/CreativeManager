package fr.k0bus.creativemanager.commands.cm;

import fr.k0bus.creativemanager.CreativeManager;
import fr.k0bus.creativemanager.commands.SubCommands;
import fr.k0bus.creativemanager.gui.settings.ProtectionSettingGui;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SettingsSubCommands extends SubCommands {

    public SettingsSubCommands(CreativeManager instance) {
        super(instance, "creativemanager.admin", true);
    }

    @Override
    protected void run(CommandSender sender, String[] args) {
        new ProtectionSettingGui(plugin).open((Player) sender);
    }
}
