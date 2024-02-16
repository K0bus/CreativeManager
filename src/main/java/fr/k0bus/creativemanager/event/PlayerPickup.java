package fr.k0bus.creativemanager.event;

import fr.k0bus.creativemanager.CreativeManager;
import fr.k0bus.creativemanager.settings.Protections;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPickupItemEvent;

/**
 * Player pickup event listener.
 */
public class PlayerPickup implements Listener {

    /**
     * Instantiates a new Player pickup.
     *
     */
    public PlayerPickup() {
    }

    /**
     * On pickup.
     *
     * @param e the event.
     */
    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void onPickup(EntityPickupItemEvent e) {
        if (!CreativeManager.getSettings().getProtection(Protections.PICKUP)) return;
        if (!(e.getEntity() instanceof Player p)) return;
        if (!p.getGameMode().equals(GameMode.CREATIVE)) return;
        if (!p.hasPermission("creativemanager.bypass.pickup")) {
            e.setCancelled(true);
        }
    }
}
