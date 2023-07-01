package fr.k0bus.creativemanager.settings;

import fr.k0bus.creativemanager.CreativeManager;
import fr.k0bus.k0buscore.config.Configuration;
import fr.k0bus.k0buscore.utils.StringUtils;

import java.util.List;

/**
 * Settings class.
 */
public class Settings extends Configuration {
    /**
     * Instantiates a new Settings.
     *
     * @param instance the instance
     */
    public Settings(CreativeManager instance) {
        super("config.yml", instance);
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
     * Gets protection.
     *
     * @param protections the protections.
     * @param value the protections.
     */
    public void setProtection(Protections protections, boolean value) {
        set("protections." + protections.getName(), value);
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
     * Spectator inv enable boolean.
     *
     * @return True if yes, otherwise false.
     */
    public boolean spectatorInvEnable() {
        return getBoolean("inventory.spectator");
    }

    /**
     * Gets place bl.
     *
     * @return the placed blocks.
     */
    public List<String> getPlaceBL() {
        return getStringList("list.place");
    }

    /**
     * Gets use bl.
     *
     * @return the use blacklist.
     */
    public List<String> getUseBL() {
        return getStringList("list.use");
    }

    /**
     * Gets use bl.
     *
     * @return the use blacklist.
     */
    public List<String> getUseBlockBL() {
        return getStringList("list.useblock");
    }

    /**
     * Gets get bl.
     *
     * @return the get blacklist.
     */
    public List<String> getGetBL() {
        return getStringList("list.get");
    }

    /**
     * Gets break bl.
     *
     * @return the break blacklist.
     */
    public List<String> getBreakBL() {
        return getStringList("list.break");
    }

    /**
     * Gets command bl.
     *
     * @return the command blacklist.
     */
    public List<String> getCommandBL() {
        return getStringList("list.commands");
    }

    /**
     * Gets lore.
     *
     * @return the lore.
     */
    public List<String> getLore() {
        return getStringList("creative-lore");
    }

    public String getLang() {
        return getString("lang");
    }
    public String getTag()
    {
        return StringUtils.translateColor(getString("tag"));
    }
}
