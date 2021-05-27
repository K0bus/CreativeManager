package fr.k0bus.creativemanager.settings;

import fr.k0bus.k0buslib.settings.Configuration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * User data class.
 */
public class UserData extends Configuration {
    /**
     * Instantiates a new User data.
     *
     * @param p        the player.
     * @param instance the instance.
     */
    public UserData(Player p, JavaPlugin instance) {
        super(p.getUniqueId() + ".yml", instance, "data");
    }
}
