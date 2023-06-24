package fr.k0bus.creativemanager.commands.cm.data;

import fr.k0bus.creativemanager.CreativeManager;
import fr.k0bus.creativemanager.commands.SubCommands;

/**
 * Main command class.
 */
public class DataSubCommands extends SubCommands {

    public DataSubCommands(CreativeManager instance) {
        super(instance, "creativemanager.admin", false);

        registerCommands("purge", new DataPurgeSubCommands(getPlugin()));
        //registerCommands("convert", new DataConvertorSubCommands(getPlugin()));
    }
}