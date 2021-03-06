package fr.k0bus.creativemanager.event;

import fr.k0bus.creativemanager.CreativeManager;
import fr.k0bus.creativemanager.settings.Protections;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPickupItemEvent;

/**
 * Player pickup event listener.
 */
public class PlayerPickup implements Listener {
    private final CreativeManager plugin;

    /**
     * Instantiates a new Player pickup.
     *
     * @param plugin the plugin.
     */
    public PlayerPickup(CreativeManager plugin) {
        this.plugin = plugin;
    }

    /**
     * On pickup.
     *
     * @param e the event.
     */
    @EventHandler
    public void onPickup(EntityPickupItemEvent e) {
        if (!plugin.getSettings().getProtection(Protections.PICKUP)) return;
        if (!(e.getEntity() instanceof Player)) return;
        Player p = (Player) e.getEntity();
        if (!p.getGameMode().equals(GameMode.CREATIVE)) return;
        if (!p.hasPermission("creativemanager.bypass.pickup")) {
            e.setCancelled(true);
        }
    }
}
