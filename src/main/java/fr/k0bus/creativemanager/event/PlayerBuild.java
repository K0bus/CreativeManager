package fr.k0bus.creativemanager.event;

import fr.k0bus.creativemanager.CreativeManager;
import fr.k0bus.creativemanager.log.BlockLog;
import fr.k0bus.creativemanager.settings.Protections;
import fr.k0bus.k0buslib.utils.Messages;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;

import java.util.HashMap;

/**
 * Player build event listener.
 */
public class PlayerBuild implements Listener {
	private final CreativeManager plugin;

	/**
	 * Instantiates a new Player build.
	 *
	 * @param instance the instance.
	 */
	public PlayerBuild(CreativeManager instance) {
		plugin = instance;
	}

	/**
	 * On place.
	 *
	 * @param e the event.
	 */
	@EventHandler(ignoreCancelled = true)
	public void onPlace(BlockPlaceEvent e) {
		Player p = e.getPlayer();
		if (p.getGameMode() == GameMode.CREATIVE) {
			if (plugin.getSettings().getProtection(Protections.BUILD) && !p.hasPermission("creativemanager.bypass.build")) {
				if (plugin.getSettings().getBoolean("send-player-messages"))
					Messages.sendMessage(plugin.getMessageManager(), p, "permission.build");
				e.setCancelled(true);
			} else if (plugin.getSettings().getPlaceBL().stream().anyMatch(e.getBlock().getType().name()::equalsIgnoreCase) && !p.hasPermission("creativemanager.bypass.blacklist.place")) {
				HashMap<String, String> replaceMap = new HashMap<>();
				replaceMap.put("{BLOCK}", e.getBlock().getType().name());
				if (plugin.getSettings().getBoolean("send-player-messages"))
					Messages.sendMessage(plugin.getMessageManager(), p, "blacklist.place", replaceMap);
				e.setCancelled(true);
			} else {
				plugin.getDataManager().addBlock(new BlockLog(e.getBlock(), e.getPlayer()));
			}
		} else {
			BlockLog blockLog = plugin.getDataManager().getBlockFrom(e.getBlock().getLocation());
			if (blockLog != null)
				if (blockLog.isCreative()) {
					plugin.getDataManager().removeBlock(blockLog.getLocation());
				}
		}

	}
}
