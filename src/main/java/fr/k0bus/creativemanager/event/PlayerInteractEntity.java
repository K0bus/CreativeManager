package fr.k0bus.creativemanager.event;

import fr.k0bus.creativemanager.CreativeManager;
import fr.k0bus.creativemanager.settings.Protections;
import fr.k0bus.k0buslib.utils.Messages;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;

/**
 * Player interact entity event listener.
 */
public class PlayerInteractEntity implements Listener {
    private final CreativeManager plugin;

    /**
     * Instantiates a new Player interact entity.
     *
     * @param instance the instance.
     */
    public PlayerInteractEntity(CreativeManager instance) {
        plugin = instance;
    }

    /**
     * On use.
     *
     * @param e the event.
     */
    @EventHandler(ignoreCancelled = true)
    public void onUse(PlayerInteractEntityEvent e) {
        Player p = e.getPlayer();
        if (plugin.getSettings().getProtection(Protections.ENTITY) && p.getGameMode().equals(GameMode.CREATIVE) && !p.hasPermission("creativemanager.bypass.entity")) {
            if (!p.hasPermission("creativemanager.bypass.entity") && !p.hasPermission("creativemanager.bypass.entity." + e.getRightClicked().getType().name().toLowerCase())) {
                if (plugin.getSettings().getBoolean("send-player-messages"))
                    Messages.sendMessage(plugin.getMessageManager(), p, "permission.entity");
                e.setCancelled(true);
            }
        }
    }
}
