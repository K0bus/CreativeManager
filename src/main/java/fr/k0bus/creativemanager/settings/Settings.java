package fr.k0bus.creativemanager.settings;

import fr.k0bus.creativemanager.CreativeManager;
import fr.k0bus.k0buscore.config.Configuration;
import fr.k0bus.k0buscore.utils.StringUtils;
import org.bukkit.GameMode;

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
     * Helper function for parse inventory names.
     *
     * @return the name of the inventory.
     */
    private String getInvNameOfGamemodeStr(String gm_str) {
        String value = getString("inventory." + gm_str);
        if (value.equalsIgnoreCase("false")) {
            return "SURVIVAL";
        } else if (value.equalsIgnoreCase("true")) {
            return gm_str.toUpperCase();
        } else {
            return getString("inventory." + gm_str);
        }
    }

    /**
     * Creative inv name string.
     *
     * @return the name of the creative inventory.
     */
    public String getCreativeInvName() {
        return getInvNameOfGamemodeStr("creative");
    }

    /**
     * Survival inv name string.
     *
     * @return the name of the survival inventory.
     */
    public String getSurvivalInvName() {
        return getInvNameOfGamemodeStr("survival");
    }

    /**
     * Adventure inv name string.
     *
     * @return the name of the adventure inventory.
     */
    public String getAdventureInvName() {
        return getInvNameOfGamemodeStr("adventure");
    }

    /**
     * Spectator inv name string.
     *
     * @return the name of the spectator inventory.
     */
    public String getSpectatorInvName() {
        return getInvNameOfGamemodeStr("spectator");
    }

    /**
     * Inv name string of the gamemode.
     *
     * @return the name of the inventory related to the given gamemode.
     */
    public String getInvNameOfGamemode(GameMode gm) {
        switch (gm) {
            case CREATIVE:
                return getCreativeInvName();
            case ADVENTURE:
                return getAdventureInvName();
            case SPECTATOR:
                return getSpectatorInvName();
            default:
                return getSurvivalInvName();
        }
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
