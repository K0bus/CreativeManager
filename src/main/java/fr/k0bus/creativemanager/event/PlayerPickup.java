package fr.k0bus.creativemanager.event;

import fr.k0bus.creativemanager.CreativeManager;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPickupItemEvent;

public class PlayerPickup implements Listener {
    CreativeManager plugin;
    public PlayerPickup(CreativeManager plugin)
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
