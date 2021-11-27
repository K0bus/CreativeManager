package fr.k0bus.creativemanager.event;

import fr.k0bus.creativemanager.CreativeManager;
import fr.k0bus.creativemanager.log.BlockLog;
import fr.k0bus.creativemanager.settings.Protections;
import fr.k0bus.k0buslib.utils.Messages;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

import java.util.HashMap;

/**
 * Player break block listener.
 */
public class PlayerBreak implements Listener {
	private final CreativeManager plugin;

	/**
	 * Instantiates a new Player break.
	 *
	 * @param instance the instance.
	 */
	public PlayerBreak(CreativeManager instance) {
		plugin = instance;
	}

	/**
	 * On block break.
	 *
	 * @param e the event.
	 */
	@EventHandler(priority = EventPriority.MONITOR)
	public void onBreak(BlockBreakEvent e) {
		if(e.isCancelled()) return;
		Player p = e.getPlayer();
		if (p.getGameMode() == GameMode.CREATIVE) {
			if (plugin.getSettings().getProtection(Protections.BUILD) && !p.hasPermission("creativemanager.bypass.build")) {
				if (plugin.getSettings().getBoolean("send-player-messages"))
					Messages.sendMessage(plugin.getMessageManager(), p, "permission.build");
				e.setCancelled(true);
			} else if (plugin.getSettings().getBreakBL().stream().anyMatch(e.getBlock().getType().name()::equalsIgnoreCase) && !p.hasPermission("creativemanager.bypass.blacklist.break")) {
				HashMap<String, String> replaceMap = new HashMap<>();
				replaceMap.put("{BLOCK}", e.getBlock().getType().name());
				if (plugin.getSettings().getBoolean("send-player-messages"))
					Messages.sendMessage(plugin.getMessageManager(), p, "blacklist.place", replaceMap);
				e.setCancelled(true);
			}
		} else {
			if (!p.hasPermission("creativemanager.bypass.log") && plugin.getSettings().getProtection(Protections.LOOT)) {
				BlockLog blockLog = plugin.getDataManager().getBlockFrom(e.getBlock().getLocation());
				if (blockLog != null) {
					if (blockLog.isCreative()) {
						e.getBlock().setType(Material.AIR);
						plugin.getDataManager().removeBlock(blockLog.getLocation());
					}
				}
			}
		}
	}
}
