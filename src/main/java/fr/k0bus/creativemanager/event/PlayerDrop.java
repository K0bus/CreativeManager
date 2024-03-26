package fr.k0bus.creativemanager.event;

import fr.k0bus.creativemanager.CreativeManager;
import fr.k0bus.creativemanager.settings.Protections;
import fr.k0bus.creativemanager.utils.CMUtils;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;

/**
 * Player drop item event listener.
 */
public class PlayerDrop implements Listener {

	/**
	 * Instantiates a new Player drop.
	 *
	 */
	CreativeManager plugin;
	public PlayerDrop(CreativeManager plugin) {
		this.plugin = plugin;
	}

	/**
	 * On drop.
	 *
	 * @param e the event.
	 */
	@EventHandler(ignoreCancelled = true)
	public void onDrop(PlayerDropItemEvent e) {
		Player p = e.getPlayer();
		if (CreativeManager.getSettings().getProtection(Protections.DROP) && p.getGameMode().equals(GameMode.CREATIVE)) {
			if (!p.hasPermission("creativemanager.bypass.drop")) {
				if (CreativeManager.getSettings().getBoolean("send-player-messages"))
					CMUtils.sendMessage(p, "permission.drop");
				e.setCancelled(true);
			}
		}
	}
}
