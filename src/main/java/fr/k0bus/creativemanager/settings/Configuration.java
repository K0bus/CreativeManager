package fr.k0bus.creativemanager.settings;

import fr.k0bus.creativemanager.CreativeManager;
import org.apache.commons.lang.Validate;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import org.yaml.snakeyaml.error.YAMLException;
import org.yaml.snakeyaml.parser.ParserException;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;

public class Configuration {

    JavaPlugin plugin;
    File file;
    FileConfiguration configuration;
    String filename;
    String defaultRessource;

    public Configuration(String filename, JavaPlugin instance)
    {
        this.plugin = instance;
        this.filename = filename;
        this.defaultRessource = filename;
        this.file = new File(instance.getDataFolder(), filename);
        loadConfig();
    }

    public Configuration(String filename, JavaPlugin instance, String dirName)
    {
        this.plugin = instance;
        this.filename = filename;
        this.defaultRessource = dirName + "/" + filename;
        File dir = new File(plugin.getDataFolder(), dirName);
        if(!dir.exists())
            dir.mkdirs();
        if(dir.isDirectory())
            this.file = new File(dir, filename);
        else
            this.file = new File(dir.getParentFile(), filename);
        loadConfig();
    }

    public void loadConfig()
    {
        if(plugin.getResource(defaultRessource) != null)
            if(!file.exists())
                plugin.saveResource(defaultRessource, false);
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
        {
            try {
                this.configuration = loadConfiguration(this.file);
            } catch (InvalidConfigurationException e)
            {
                plugin.getLogger().log(Level.SEVERE, filename + " can't be loaded ! Check file syntax first !");
                plugin.getLogger().log(Level.SEVERE, e.getMessage());
                file.renameTo(new File(file.getParentFile(), filename + ".old"));
                loadConfig();
            }
            catch (IOException ex)
            {
                plugin.getLogger().log(Level.SEVERE, "Can't read file " + filename);
                this.configuration = new YamlConfiguration();
            }
        }
        else
        {
            this.configuration = new YamlConfiguration();
        }
    }

    private static YamlConfiguration loadConfiguration(File file) throws IOException, InvalidConfigurationException {
        Validate.notNull(file, "File cannot be null");

        YamlConfiguration config = new YamlConfiguration();

        try {
            config.load(file);
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            throw ex;
        } catch (InvalidConfigurationException ex) {
            throw ex;
        }

        return config;
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
