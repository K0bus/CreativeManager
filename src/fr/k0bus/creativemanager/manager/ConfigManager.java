package fr.k0bus.creativemanager.manager;

import java.io.File;
import java.io.IOException;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public class ConfigManager {

    String name;
    JavaPlugin plugin;
    File path;
    File file;
    FileConfiguration conf;
    
    public ConfigManager(String files, File path, JavaPlugin plugin)
    {
        this.name = files;
        this.path = path;
        this.plugin = plugin;

        this.loadConfig();
    }
    private void loadConfig()
    {
        this.file = new File(path, this.name);
        if(!this.file.exists())
        {
            this.file.getParentFile().mkdirs();
            if(this.name.equals("config.yml"))
            {
                plugin.saveResource("config.yml", false);
            }
            else
            {
                try {
                    this.file.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        this.conf = new YamlConfiguration();
        try{
            this.conf.load(this.file);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }
    }
    public void saveConfig()
    {
        try {
            this.conf.save(this.file);
        } catch(IOException e)
        {
            e.printStackTrace();
        }
    }
    public FileConfiguration getConfig()
    {
        return this.conf;
    }
    
}