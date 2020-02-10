package fr.k0bus.creativemanager;

import java.util.logging.Level;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import fr.k0bus.creativemanager.commands.*;
import fr.k0bus.creativemanager.event.*;
import fr.k0bus.creativemanager.manager.ConfigManager;

public class Main extends JavaPlugin {

    public ConfigManager mainConf;

    @Override
    public void onEnable() {
        this.getLogger().log(Level.INFO, "=============================================================");
        this.getLogger().log(Level.INFO, " _____                _   _          ___  ___");
        this.getLogger().log(Level.INFO, "/  __ \\              | | (_)         |  \\/  |");
        this.getLogger().log(Level.INFO, "| /  \\/_ __ ___  __ _| |_ ___   _____| .  . |");
        this.getLogger().log(Level.INFO, "| |   | '__/ _ \\/ _` | __| \\ \\ / / _ \\ |\\/| |");
        this.getLogger().log(Level.INFO, "| \\__/\\ | |  __/ (_| | |_| |\\ V /  __/ |  | |");
        this.getLogger().log(Level.INFO, " \\____/_|  \\___|\\__,_|\\__|_| \\_/ \\___\\_|  |_/");
        this.getLogger().log(Level.INFO, "=============================================================");
        this.getLogger().log(Level.INFO, "Created by K0bus for AkuraGaming");
        this.getLogger().log(Level.INFO, "=============================================================");
        this.loadConfigManager();
        this.registerEvent(this.getServer().getPluginManager());
        this.registerCommand();
        this.getLogger().log(Level.INFO, "=============================================================");
    }

    public void loadConfigManager() {
        this.getLogger().log(Level.INFO, "Loading configuration ...");
        this.mainConf = new ConfigManager("config.yml", this.getDataFolder(), this);
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
        this.getLogger().log(Level.INFO, "Event loaded successfully !");
    }
    private void registerCommand()
    {
        this.getCommand("cm").setExecutor(new MainCommand(this));
    }
    public FileConfiguration getConfig()
    {
        return this.mainConf.getConfig();
    }
    @Override
    public void onDisable()
    {
        
    }
}