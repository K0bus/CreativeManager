package fr.k0bus.creativemanager.event;

import fr.k0bus.creativemanager.CreativeManager;
import fr.k0bus.creativemanager.settings.Protections;
import fr.k0bus.k0buslib.utils.Messages;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;

/**
 * Player drop item event listener.
 */
public class PlayerDrop implements Listener {
	private final CreativeManager plugin;

	/**
	 * Instantiates a new Player drop.
	 *
	 * @param instance the instance.
	 */
	public PlayerDrop(CreativeManager instance) {
		plugin = instance;
	}

	/**
	 * On drop.
	 *
	 * @param e the event.
	 */
	@EventHandler(ignoreCancelled = true)
	public void onDrop(PlayerDropItemEvent e) {
		Player p = e.getPlayer();
		if (plugin.getSettings().getProtection(Protections.DROP) && p.getGameMode().equals(GameMode.CREATIVE)) {
			if (!p.hasPermission("creativemanager.bypass.drop")) {
				if (plugin.getSettings().getBoolean("send-player-messages"))
					Messages.sendMessage(plugin.getMessageManager(), p, "permission.drop");
				e.setCancelled(true);
			}
		}
	}
}
