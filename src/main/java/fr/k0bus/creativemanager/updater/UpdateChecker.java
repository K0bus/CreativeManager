package fr.k0bus.creativemanager.updater;

import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Scanner;
import java.util.logging.Level;

public class UpdateChecker {

    private final JavaPlugin plugin;
    private final String version;
    private final String spigotVersion;
    private final int resourceId;

    public UpdateChecker(JavaPlugin plugin, int resourceId) {
        this.plugin = plugin;
        this.version = this.plugin.getDescription().getVersion();
        this.resourceId = resourceId;
        this.spigotVersion = getVersion();
    }

    public String getVersion() {
        try (InputStream inputStream = new URL("https://api.spigotmc.org/legacy/update.php?resource=" + this.resourceId).openStream(); Scanner scanner = new Scanner(inputStream)) {
            if (scanner.hasNext()) {
                return scanner.next();
            }
        } catch (IOException exception) {
            this.plugin.getLogger().info("Cannot look for updates: " + exception.getMessage());
        }
        return null;
    }
    public boolean isUpToDate()
    {
        return spigotVersion.compareTo(this.version)<0;
    }
    public void checkUpdate()
    {
        if(spigotVersion != null)
            if(isUpToDate())
                plugin.getLogger().log(Level.INFO, plugin.getDescription().getName() + " is up to date");
            else
                plugin.getLogger().log(Level.WARNING, plugin.getDescription().getName() + " can be updated - Actual version : " + version + " - New version : " + spigotVersion);
    }
}
