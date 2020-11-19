package fr.k0bus.creativemanager.settings;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Set;

public class Configuration {

    JavaPlugin plugin;
    File file;
    FileConfiguration configuration;

    public Configuration(String filename, JavaPlugin instance)
    {
        this.plugin = instance;
        this.file = new File(instance.getDataFolder(), filename);
        if(plugin.getResource(filename) != null)
            plugin.saveResource(filename, false);
        else
        {
            this.file.mkdirs();
            try {
                this.file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if(this.file.exists())
            this.configuration = YamlConfiguration.loadConfiguration(this.file);
    }
    public Configuration(String filename, JavaPlugin instance, String dirName)
    {
        this.plugin = instance;
        File dir = new File(plugin.getDataFolder(), dirName);
        if(!dir.exists())
            dir.mkdirs();
        if(dir.isDirectory())
            this.file = new File(dir, filename);
        else
            this.file = new File(dir.getParentFile(), filename);
        if(plugin.getResource(filename) != null)
            plugin.saveResource(filename, false);
        else
        {
            try {
                this.file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if(this.file.exists())
            this.configuration = YamlConfiguration.loadConfiguration(this.file);
    }
    public Configuration(File file, JavaPlugin instance)
    {
        this.plugin = instance;
        this.file = file;
        if(this.file.exists())
            this.configuration = YamlConfiguration.loadConfiguration(this.file);
    }
    public void save()
    {
        try {
            this.configuration.save(this.file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public String getString(String path)
    {
        return this.configuration.getString(path);
    }
    public boolean getBoolean(String path)
    {
        return this.configuration.getBoolean(path);
    }
    public int getInt(String path)
    {
        return this.configuration.getInt(path);
    }
    public double getDouble(String path)
    {
        return this.configuration.getDouble(path);
    }
    public ConfigurationSection getConfigurationSection(String path){return this.configuration.getConfigurationSection(path);}
    public boolean contains(String path){ return this.configuration.contains(path); }
    public boolean isString(String path) { return this.configuration.isString(path); }
    public Set<String> getKeys(boolean deep){return this.configuration.getKeys(deep); }
    public Set<String> getKeysFromPath(String path, boolean deep){return this.configuration.getConfigurationSection(path).getKeys(deep); }
    public List<String> getStringList(String path){return this.configuration.getStringList(path);}

    public File getFile() {
        return file;
    }

    public void set(String path, Object o)
    {
        this.configuration.set(path, o);
        try {
            this.configuration.save(this.file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
