package fr.k0bus.creativemanager.settings;

import org.bukkit.plugin.java.JavaPlugin;

public class Language extends Configuration{
    public Language(String lang, JavaPlugin instance) {
        super(lang + ".yml", instance, "lang");
    }
}
