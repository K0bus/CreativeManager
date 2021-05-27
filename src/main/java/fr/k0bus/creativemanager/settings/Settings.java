package fr.k0bus.creativemanager.settings;

import fr.k0bus.creativemanager.CreativeManager;

import java.util.List;

/**
 * Settings class.
 */
public class Settings extends fr.k0bus.k0buslib.settings.Settings {
    /**
     * Instantiates a new Settings.
     *
     * @param instance the instance
     */
    public Settings(CreativeManager instance) {
        super(instance);
    }

    /**
     * Is logged boolean.
     *
     * @return True if yes, otherwise false.
     */
    public boolean isLogged() {
        return getBoolean("log");
    }

    /**
     * Gets protection.
     *
     * @param protections the protections.
     * @return the protection.
     */
    public boolean getProtection(Protections protections) {
        return getBoolean("protections." + protections.getName());
    }

    /**
     * Creative inv enable boolean.
     *
     * @return True if yes, otherwise false.
     */
    public boolean creativeInvEnable() {
        return getBoolean("inventory.creative");
    }

    /**
     * Adventure inv enable boolean.
     *
     * @return True if yes, otherwise false.
     */
    public boolean adventureInvEnable() {
        return getBoolean("inventory.adventure");
    }

    /**
     * Gets place bl.
     *
     * @return the placed blocks.
     */
    public List<String> getPlaceBL() {
        return getStringList("blacklist.place");
    }

    /**
     * Gets use bl.
     *
     * @return the use blacklist.
     */
    public List<String> getUseBL() {
        return getStringList("blacklist.use");
    }

    /**
     * Gets get bl.
     *
     * @return the get blacklist.
     */
    public List<String> getGetBL() {
        return getStringList("blacklist.get");
    }

    /**
     * Gets break bl.
     *
     * @return the break blacklist.
     */
    public List<String> getBreakBL() {
        return getStringList("blacklist.break");
    }

    /**
     * Gets command bl.
     *
     * @return the command blacklist.
     */
    public List<String> getCommandBL() {
        return getStringList("blacklist.commands");
    }

    /**
     * Gets lore.
     *
     * @return the lore.
     */
    public List<String> getLore() {
        return getStringList("creative-lore");
    }
}
