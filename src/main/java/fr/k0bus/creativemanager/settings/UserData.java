package fr.k0bus.creativemanager.settings;

import fr.k0bus.k0buslib.settings.Configuration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class UserData extends Configuration {
    public UserData(Player p, JavaPlugin instance) {
        super(p.getUniqueId().toString() + ".yml", instance, "data");
    }

}
