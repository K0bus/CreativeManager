package fr.k0bus.creativemanager.settings;

import fr.k0bus.creativemanager.CreativeManager;
import net.md_5.bungee.api.ChatColor;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class Settings extends Configuration{
    public Settings(CreativeManager instance) {
        super("config.yml", instance);
    }
    public String getTag()
    {
        return ChatColor.translateAlternateColorCodes('&', getString("tag"));
    }
    public String getLang()
    {
        return getString("lang");
    }
    public boolean isLogged()
    {
        return getBoolean("log");
    }
    public boolean getProtection(Protections protections)
    {
        return getBoolean("protections." + protections.getName());
    }
    public boolean creativeInvEnable()
    {
        return getBoolean("inventory.creative");
    }
    public boolean adventureInvEnable()
    {
        return getBoolean("inventory.adventure");
    }
    public List<String> getPlaceBL()
    {
        return getStringList("blacklist.place");
    }
    public List<String> getUseBL()
    {
        return getStringList("blacklist.use");
    }
    public List<String> getGetBL()
    {
        return getStringList("blacklist.get");
    }
    public List<String> getLore()
    {
        return getStringList("creative-lore");
    }
}
