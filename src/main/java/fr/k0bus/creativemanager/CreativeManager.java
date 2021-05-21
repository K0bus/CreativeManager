package fr.k0bus.creativemanager;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.logging.Level;

import com.google.common.io.Files;
import fr.k0bus.creativemanager.log.BlockLog;
import fr.k0bus.creativemanager.log.DataManager;
import fr.k0bus.k0buslib.settings.Configuration;
import fr.k0bus.k0buslib.settings.Lang;
import fr.k0bus.creativemanager.settings.Settings;
import fr.k0bus.k0buslib.updater.UpdateChecker;
import fr.k0bus.k0buslib.utils.Messages;
import fr.k0bus.k0buslib.utils.MessagesManager;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.PluginCommand;
import org.bukkit.entity.EntityType;
import org.bukkit.permissions.Permission;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import fr.k0bus.creativemanager.commands.*;
import fr.k0bus.creativemanager.event.*;

public class CreativeManager extends JavaPlugin {

    private Settings settings;
    private Lang lang;
    private final String invTag = ChatColor.BOLD + "" + ChatColor.DARK_RED + "CM " + ChatColor.RESET + "> ";
    private MessagesManager messagesManager;
    private DataManager dataManager;
    private int task;

    @Override
    public void onEnable() {
        Messages.log(this, "&9=============================================================");
        UpdateChecker updateChecker = new UpdateChecker(this, 75097);
        if(updateChecker.isUpToDate())
        {
            Messages.log(this, "&2CreativeManager &av" + this.getDescription().getVersion());
        }
        else
        {
            Messages.log(this, "&2CreativeManager &cv" + this.getDescription().getVersion() +
                    " (Update " + updateChecker.getVersion() + " available on SpigotMC)");
        }
        Messages.log(this, "&9=============================================================");
        Messages.log(this, "&2Created by K0bus for AkuraGaming");
        Messages.log(this, "&9=============================================================");
        this.updateConfig();
        this.loadConfigManager();
        this.registerEvent(this.getServer().getPluginManager());
        Messages.log(this, "&2Listener registered !");
        this.registerCommand();
        Messages.log(this, "&2Commands registered !");
        this.registerPermissions();
        this.loadLog();
        this.startAutoSave();
        this.messagesManager = new MessagesManager(getSettings(), getLang());
        Messages.log(this, "&9=============================================================");
    }

    public void loadConfigManager() {
        this.settings = new Settings(this);
        Messages.log(this, "&2Configuration loaded !");
        this.lang = new Lang(settings.getLang(), this);
        Messages.log(this, "&2Language loaded ! &7[" + settings.getLang() + "]");
    }
    public void updateConfig()
    {
        Configuration oldConfig = new Configuration("config.yml", this);
        new Lang("en_EN", this);
        new Lang("es_ES", this);
        new Lang("fr_FR", this);
        new Lang("it_IT", this);
        new Lang("ru_RU", this);
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
    private void registerPermissions()
    {
        PluginManager pm = getServer().getPluginManager();
        Set<Permission> permissions = pm.getPermissions();
        int n = 0;
        for (EntityType entityType: EntityType.values()) {
            Permission perm = new Permission("creativemanager.bypass.entity." + entityType.name());
            if(!permissions.contains(perm))
            {
                pm.addPermission(perm);
                n++;
            }
        }
        Messages.log(this, "&2Entities permissions registered ! &7[" + n + "]");
    }
    private void loadLog()
    {
        dataManager = new DataManager("data");
        dataManager.loadLog();
        Messages.log(this, "&2Log loaded from database ! &7[" + dataManager.getBlockBlockLogMap().size() + "]");
    }
    public Settings getSettings()
    {
        return this.settings;
    }
    public Lang getLang()
    {
        return this.lang;
    }

    public String getInvTag() {
        return invTag;
    }

    public DataManager getDataManager() {
        return dataManager;
    }

    public void startAutoSave()
    {
        if(Bukkit.getScheduler().isCurrentlyRunning(this.task) || Bukkit.getScheduler().isQueued(this.task))
            this.stopAutoSave();
        Messages.log(this, "&2Start autosave task");
        this.task = Bukkit.getScheduler().scheduleSyncRepeatingTask(this, () -> {
            int saved = 0;
            int deleted = 0;
            for(Map.Entry<Location, BlockLog> log: dataManager.getBlockBlockLogMap().entrySet()) {
                Connection conn = new DataManager("data").getConn();
                try {
                    PreparedStatement ps = conn.prepareStatement("SELECT count(*) FROM block_log WHERE uuid=?");
                    ps.setString(1, log.getValue().getUuid().toString());
                    ResultSet rs = ps.executeQuery();
                    ps.close();
                    if(rs.next())
                    {
                        if(rs.getInt("total") <= 0)
                        {
                            log.getValue().save();
                            saved++;
                        }
                    }
                } catch (SQLException ex) {
                    Bukkit.getLogger().log(Level.SEVERE, "Unable to retrieve connection", ex);
                }
            }
            for(UUID uuid: dataManager.getToDelete())
            {
                Connection conn = new DataManager("data").getConn();
                try {
                    PreparedStatement ps = conn.prepareStatement("DELETE FROM block_log WHERE uuid=?");
                    ps.setString(1, uuid.toString());
                    ps.executeUpdate();
                    ps.close();
                    deleted++;
                    dataManager.resetToDelete();
                } catch (SQLException ex) {
                    Bukkit.getLogger().log(Level.SEVERE, "Unable to retrieve connection", ex);
                }
            }
            //Messages.log(plugin, "&2Log saved to database ! &7[&cdelete " + deleted + "&7] &7[&aadded " + saved + "&7]");
        }, 0L, 30*20);
    }
    public void stopAutoSave()
    {
        if(Bukkit.getScheduler().isCurrentlyRunning(this.task) || Bukkit.getScheduler().isQueued(this.task))
            Bukkit.getScheduler().cancelTask(this.task);
        Messages.log(this, "&2Stop autosave task");
    }

    @Override
    public void onDisable()
    {
        
    }

    public MessagesManager getMessageManager() {
        return messagesManager;
    }
}
