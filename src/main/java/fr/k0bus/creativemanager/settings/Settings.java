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
     * Gets protection.
     *
     * @param protections the protections.
     * @return the protection.
     */
    public boolean getProtection(Protections protections) {
        return getConfiguration().getBoolean("protections." + protections.getName());
    }

    /**
     * Gets protection.
     *
     * @param protections the protections.
     * @param value the protections.
     */
    public void setProtection(Protections protections, boolean value) {
        getConfiguration().set("protections." + protections.getName(), value);
    }

    /**
     * Creative inv enable boolean.
     *
     * @return True if yes, otherwise false.
     */
    public boolean creativeInvEnable() {
        return getConfiguration().getBoolean("inventory.creative");
    }

    /**
     * Adventure inv enable boolean.
     *
     * @return True if yes, otherwise false.
     */
    public boolean adventureInvEnable() {
        return getConfiguration().getBoolean("inventory.adventure");
    }

    /**
     * Spectator inv enable boolean.
     *
     * @return True if yes, otherwise false.
     */
    public boolean spectatorInvEnable() {
        return getConfiguration().getBoolean("inventory.spectator");
    }

    /**
     * Gets place bl.
     *
     * @return the placed blocks.
     */
    public List<String> getPlaceBL() {
        return getConfiguration().getStringList("list.place");
    }

    /**
     * Gets use bl.
     *
     * @return the use blacklist.
     */
    public List<String> getUseBL() {
        return getConfiguration().getStringList("list.use");
    }

    /**
     * Gets use bl.
     *
     * @return the use blacklist.
     */
    public List<String> getUseBlockBL() {
        return getConfiguration().getStringList("list.useblock");
    }

    /**
     * Gets get bl.
     *
     * @return the get blacklist.
     */
    public List<String> getGetBL() {
        return getConfiguration().getStringList("list.get");
    }

    /**
     * Gets break bl.
     *
     * @return the break blacklist.
     */
    public List<String> getBreakBL() {
        return getConfiguration().getStringList("list.break");
    }

    /**
     * Gets command bl.
     *
     * @return the command blacklist.
     */
    public List<String> getCommandBL() {
        return getConfiguration().getStringList("list.commands");
    }

    /**
     * Gets command bl.
     *
     * @return the command blacklist.
     */
    public List<String> getNBTWhitelist() {
        return getConfiguration().getStringList("list.nbt-whitelist");
    }

    /**
     * Gets lore.
     *
     * @return the lore.
     */
    public List<String> getLore() {
        return getConfiguration().getStringList("creative-lore");
    }

    public String getLang() {
        return getConfiguration().getString("lang");
    }
    public String getTag()
    {
        return StringUtils.translateColor(getConfiguration().getString("tag"));
    }
}
