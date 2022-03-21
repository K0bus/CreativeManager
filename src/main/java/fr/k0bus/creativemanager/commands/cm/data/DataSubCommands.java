package fr.k0bus.creativemanager.commands.cm.data;

import fr.k0bus.creativemanager.CreativeManager;
import fr.k0bus.creativemanager.commands.SubCommands;
import fr.k0bus.creativemanager.commands.cm.InfosSubCommands;
import fr.k0bus.creativemanager.commands.cm.ReloadSubCommands;
import fr.k0bus.creativemanager.commands.cm.SettingsSubCommands;
import org.bukkit.command.CommandSender;

/**
 * Main command class.
 */
public class DataSubCommands extends SubCommands {

    public DataSubCommands(CreativeManager instance) {
        super(instance, "creativemanager.admin", false);

        registerCommands("purge", new DataPurgeSubCommands(getPlugin()));
    }
}