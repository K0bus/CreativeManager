package fr.k0bus.creativemanager.event;

import fr.k0bus.creativemanager.CreativeManager;
import fr.k0bus.creativemanager.manager.InventoryManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;

/**
 * Player quit/kick event listeners.
 */
public class PlayerQuit implements Listener {
	private final CreativeManager plugin;

	/**
	 * Instantiates a new Player quit.
	 *
	 * @param instance the instance.
	 */
	public PlayerQuit(CreativeManager instance) {
		plugin = instance;
	}

	/**
	 * On player quit.
	 *
	 * @param e the event.
	 */
	@EventHandler(ignoreCancelled = true, priority = EventPriority.LOWEST)
	public void onQuit(PlayerQuitEvent e) {
		Player p = e.getPlayer();
		InventoryManager im = new InventoryManager(p, plugin);
		im.saveInventory(p.getGameMode());
	}

	/**
	 * On player kicked.
	 *
	 * @param e the event.
	 */
	@EventHandler(ignoreCancelled = true, priority = EventPriority.LOWEST)
	public void onKicked(PlayerKickEvent e) {
		Player p = e.getPlayer();
		InventoryManager im = new InventoryManager(p, plugin);
		im.saveInventory(p.getGameMode());
	}
}
