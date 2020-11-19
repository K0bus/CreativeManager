package fr.k0bus.creativemanager;

import java.io.File;
import java.util.logging.Level;

import fr.k0bus.kupdatechecker.UpdateChecker;
import org.bukkit.command.PluginCommand;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import fr.k0bus.creativemanager.commands.*;
import fr.k0bus.creativemanager.event.*;
import fr.k0bus.creativemanager.manager.ConfigManager;
import fr.k0bus.creativemanager.type.ConfigType;

public class CreativeManager extends JavaPlugin {

    public ConfigManager mainConf;
    public ConfigManager lang;

    @Override
    public void onEnable() {
        this.getLogger().log(Level.INFO, "=============================================================");
        this.getLogger().log(Level.INFO, "CreativeManager v" + this.getDescription().getVersion());
        this.getLogger().log(Level.INFO, "=============================================================");
        this.getLogger().log(Level.INFO, "Created by K0bus for AkuraGaming");
        this.getLogger().log(Level.INFO, "=============================================================");
        this.loadConfigManager();
        this.registerEvent(this.getServer().getPluginManager());
        this.registerCommand();
        new UpdateChecker(this, 75097).checkUpdate();
        this.getLogger().log(Level.INFO, "=============================================================");
    }

    public void loadConfigManager() {
        this.getLogger().log(Level.INFO, "Loading configuration ...");
        this.mainConf = new ConfigManager("config.yml", this.getDataFolder(), this, ConfigType.CONFIG);
        this.lang = new ConfigManager(this.getConfig().getString("lang") + ".yml", new File(this.getDataFolder(), "lang"), this, ConfigType.LANG);
        this.getLogger().log(Level.INFO, "Configuration loaded successfully !");
    }
	private void registerEvent(PluginManager pm)
	{
        this.getLogger().log(Level.INFO, "Loading event ...");
		pm.registerEvents(new PlayerBuild(this), this);
		pm.registerEvents(new PlayerBreak(this), this);
        pm.registerEvents(new PlayerInteract(this), this);
        pm.registerEvents(new PlayerInteractEntity(this), this);
        pm.registerEvents(new PlayerInteractAtEntity(this), this);
        pm.registerEvents(new PlayerDrop(this), this);
        pm.registerEvents(new InventoryMove(this), this);
        pm.registerEvents(new PlayerGamemodeChange(this), this);
        pm.registerEvents(new PlayerHitEvent(this), this);
        pm.registerEvents(new PlayerQuit(this), this);
        pm.registerEvents(new PlayerLogin(this), this);
        this.getLogger().log(Level.INFO, "Event loaded successfully !");
    }
    private void registerCommand()
    {
        PluginCommand mainCommand = this.getCommand("cm");
        if(mainCommand != null)
        {
            mainCommand.setExecutor(new MainCommand(this));
            mainCommand.setTabCompleter(new MainCommandTab());
        }
    }
    public FileConfiguration getConfig()
    {
        return this.mainConf.getConfig();
    }
    public FileConfiguration getLang()
    {
        return this.lang.getConfig();
    }
    @Override
    public void onDisable()
    {
        
    }
}
