package fr.k0bus.creativemanager.event;

import fr.k0bus.creativemanager.CreativeManager;
import fr.k0bus.creativemanager.manager.InventoryManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

/**
 * The type Player login.
 */
public class PlayerLogin implements Listener {
    private final CreativeManager plugin;

    /**
     * Instantiates a new Player login.
     *
     * @param plugin the plugin.
     */
    public PlayerLogin(CreativeManager plugin) {
        this.plugin = plugin;
    }

    /**
     * On player login/join.
     *
     * @param e the event.
     */
    @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
    public void onLogin(PlayerJoinEvent e) {
        boolean forceGamemode = false;

        try {
            BufferedReader is = new BufferedReader(new FileReader("server.properties"));
            Properties props = new Properties();
            props.load(is);
            is.close();
            forceGamemode = Boolean.parseBoolean(props.getProperty("force-gamemode"));
        } catch (IOException exception) {
            //
        }
        if (forceGamemode) {
            if (!e.getPlayer().hasPermission("creativemanager.bypass.inventory")) {
                InventoryManager im = new InventoryManager(e.getPlayer(), plugin);
                if(im.hasContent())
                    im.loadInventory(plugin.getServer().getDefaultGameMode());
                else
                    im.saveInventory(e.getPlayer().getGameMode());
            }
        }
    }
}
