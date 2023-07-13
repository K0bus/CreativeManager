package fr.k0bus.creativemanager.commands.cm;

import fr.k0bus.creativemanager.CreativeManager;
import fr.k0bus.creativemanager.commands.Commands;

/**
 * Main command class.
 */
public class CreativeManagerCommands extends Commands {

    public CreativeManagerCommands(CreativeManager instance) {
        super(instance);
        registerCommands("reload", new ReloadSubCommands(getPlugin()));
        registerCommands("settings", new SettingsSubCommands(getPlugin()));
        registerCommands("infos", new InfosSubCommands(getPlugin()));
        registerCommands("items", new ItemsSubCommands(getPlugin()));
        setDefaultSubCmd("infos");
    }
}