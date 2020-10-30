package fr.k0bus.creativemanager.event;

import fr.k0bus.creativemanager.Main;
import fr.k0bus.creativemanager.manager.InventoryManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

public class PlayerLogin implements Listener {

    private Main plugin;

    public PlayerLogin(Main plugin)
    {
        this.plugin = plugin;
    }
    @EventHandler(ignoreCancelled = true)
    public void onLogin(PlayerJoinEvent e)
    {
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
        System.out.println("Force GM : " + forceGamemode);
        if (forceGamemode)
        {
            InventoryManager im = new InventoryManager(e.getPlayer(), plugin);
            im.loadInventory(plugin.getServer().getDefaultGameMode());
        }
    }
}
