package fr.k0bus.creativemanager.commands.cm;

import fr.k0bus.creativemanager.CreativeManager;
import fr.k0bus.creativemanager.commands.CustomCommands;
import fr.k0bus.creativemanager.commands.SubCommands;

/**
 * Main command class.
 */
public class CreativeManagerCommands extends SubCommands {

    public CreativeManagerCommands(CreativeManager instance) {
        super(instance);
        registerCommands("reload", new ReloadSubCommands(getPlugin()));
        registerCommands("settings", new SettingsSubCommands(getPlugin()));
        registerCommands("infos", new InfosSubCommands(getPlugin()));
        setDefaultSubCmd("infos");
    }
}