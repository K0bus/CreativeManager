package fr.k0bus.creativemanager.manager;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.logging.Level;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import fr.k0bus.creativemanager.type.ConfigType;

public class ConfigManager {

    String name;
    JavaPlugin plugin;
    File path;
    File file;
    String type;
    FileConfiguration conf;
    
    public ConfigManager(String files, File path, JavaPlugin plugin,String type)
    {
        this.name = files;
        this.path = path;
        this.type = type;
        this.plugin = plugin;

        this.loadConfig();
    }
	private void loadConfig()
    {
        this.file = new File(path, this.name);
        if(this.type.equals(ConfigType.CONFIG))
        {
            updateConfig("config.yml");
        }
        if(this.type.equals(ConfigType.LANG))
        {
            updateConfig("lang/en_EN.yml");
            updateConfig("lang/fr_FR.yml");
        }
        
        if(!this.file.exists())
        {
            if(!file.exists())
            {
                try {
                    this.file.getParentFile().mkdirs();
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
    private void updateConfig(String cfg)
    {
        File file = new File(this.plugin.getDataFolder(), cfg);
        file.getParentFile().mkdirs();
        if(!file.exists())
            plugin.saveResource(cfg, false);
        FileConfiguration default_conf = YamlConfiguration.loadConfiguration(new InputStreamReader(plugin.getResource(cfg)));
        FileConfiguration conf = YamlConfiguration.loadConfiguration(this.file);
        for (String path : default_conf.getKeys(true)) {
            if(!conf.contains(path) || !conf.get(path).getClass().getName().equals(default_conf.get(path).getClass().getName()))
            {
                plugin.getLogger().log(Level.WARNING, path + " added to " + cfg);
                conf.set(path, default_conf.get(path));
            }
        }
        for (String path : conf.getKeys(true)) {
            if(!default_conf.contains(path) || !conf.get(path).getClass().getName().equals(default_conf.get(path).getClass().getName()))
            {
                plugin.getLogger().log(Level.WARNING, path + " removed to " + cfg);
                conf.set(path, null);
            }
        }
        try {
            conf.save(file);
        } catch (IOException e) {
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