package fr.k0bus.creativemanager.event;

import fr.k0bus.creativemanager.Main;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class PlayerPickup implements Listener {
    Main plugin;
    public PlayerPickup(Main plugin)
    {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPickup(EntityPickupItemEvent e)
    {
        if(!(e.getEntity() instanceof Player)) return;
        Player p = (Player) e.getEntity();
        if(plugin.getConfig().getBoolean("pickup-protection") && p.getGameMode().equals(GameMode.CREATIVE))
        {
            if (!p.hasPermission("creativemanager.pickup")) {
                e.setCancelled(true);
            }
        }
    }
}
