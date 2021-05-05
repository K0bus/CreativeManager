package fr.k0bus.creativemanager;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.UUID;
import java.util.logging.Level;

import com.google.common.io.Files;
import fr.k0bus.creativemanager.settings.Configuration;
import fr.k0bus.creativemanager.settings.Language;
import fr.k0bus.creativemanager.settings.Settings;
import fr.k0bus.creativemanager.updater.UpdateChecker;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import fr.k0bus.creativemanager.commands.*;
import fr.k0bus.creativemanager.event.*;

public class CreativeManager extends JavaPlugin {

    public Settings settings;
    public Language lang;
    public final String invTag = ChatColor.BOLD + "" + ChatColor.DARK_RED + "CM " + ChatColor.RESET + "> ";
    HashMap<UUID, Long> antiSpam = new HashMap<UUID, Long>();

    @Override
    public void onEnable() {
        this.getLogger().log(Level.INFO, "=============================================================");
        this.getLogger().log(Level.INFO, "CreativeManager v" + this.getDescription().getVersion());
        this.getLogger().log(Level.INFO, "=============================================================");
        this.getLogger().log(Level.INFO, "Created by K0bus for AkuraGaming");
        this.getLogger().log(Level.INFO, "=============================================================");
        this.updateConfig();
        this.loadConfigManager();
        this.registerEvent(this.getServer().getPluginManager());
        this.registerCommand();
        new UpdateChecker(this, 75097).checkUpdate();
        this.getLogger().log(Level.INFO, "=============================================================");
    }

    public void loadConfigManager() {
        this.getLogger().log(Level.INFO, "Loading configuration ...");
        this.settings = new Settings(this);
        this.lang = new Language(settings.getLang(), this);
        this.getLogger().log(Level.INFO, "Configuration loaded successfully !");
    }
    public void updateConfig()
    {
        Configuration oldConfig = new Configuration("config.yml", this);
        new Language("en_EN", this);
        new Language("es_ES", this);
        new Language("fr_FR", this);
        new Language("it_IT", this);
        new Language("ru_RU", this);
        if(oldConfig.contains("build-protection"))
        {
            try {
                Files.move(oldConfig.getFile(),new File(oldConfig.getFile().getParentFile(), "old_config.yml"));
                oldConfig = new Configuration("old_config.yml", this);
                this.settings = new Settings(this);
                this.settings.set("protections.entity", oldConfig.getBoolean("entity-protection"));
                this.settings.set("protections.pvp", oldConfig.getBoolean("pvp-protection"));
                this.settings.set("protections.container", oldConfig.getBoolean("container-protection"));
                this.settings.set("protections.spawn", oldConfig.getBoolean("spawn-protection"));
                this.settings.set("protections.drop", oldConfig.getBoolean("drop-protection"));
                this.settings.set("protections.pve", oldConfig.getBoolean("hitmonster-protection"));
                this.settings.set("protections.lore", oldConfig.getBoolean("add-lore"));
                this.settings.set("inventory.adventure", oldConfig.getBoolean("adventure-inventory"));
                this.settings.set("inventory.creative", oldConfig.getBoolean("creative-inventory"));
                this.settings.set("tag", oldConfig.getString("tag"));
                this.settings.set("lang", oldConfig.getString("lang"));
                this.settings.set("log", oldConfig.getBoolean("log"));
                this.settings.set("blacklist", oldConfig.getConfigurationSection("blacklist"));
                this.settings.set("blacklist", oldConfig.getConfigurationSection("blacklist"));
                this.settings.save();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
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
        pm.registerEvents(new PistonEvent(this), this);
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
    public Settings getSettings()
    {
        return this.settings;
    }
    public Language getLang()
    {
        return this.lang;
    }

    public String getInvTag() {
        return invTag;
    }

    public HashMap<UUID, Long> getAntiSpam() {
        return antiSpam;
    }

    @Override
    public void onDisable()
    {
        
    }
}
