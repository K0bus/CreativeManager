package fr.k0bus.creativemanager.event;

import fr.k0bus.creativemanager.CreativeManager;
import fr.k0bus.creativemanager.log.BlockLog;
import fr.k0bus.creativemanager.settings.Protections;
import fr.k0bus.creativemanager.utils.SearchUtils;
import fr.k0bus.k0buscore.utils.StringUtils;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;

import java.util.HashMap;
import java.util.List;

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
	@EventHandler(priority = EventPriority.HIGHEST)
	public void checkBuild(BlockPlaceEvent e) {
		Player p = e.getPlayer();
		if (p.getGameMode() == GameMode.CREATIVE) {
			if (CreativeManager.getSettings().getProtection(Protections.BUILD) && !p.hasPermission("creativemanager.bypass.build")) {
				if (CreativeManager.getSettings().getBoolean("send-player-messages"))
					CreativeManager.sendMessage(p, CreativeManager.TAG + CreativeManager.getLang().getString("permission.build"));
				e.setCancelled(true);
			}
		}
	}
	/**
	 * On place.
	 *
	 * @param e the event.
	 */
	@EventHandler
	public void checkBlackList(BlockPlaceEvent e)
	{
		if(e.isCancelled()) return;
		Player p = e.getPlayer();
		if(!p.getGameMode().equals(GameMode.CREATIVE)) return;
		List<String> blacklist = CreativeManager.getSettings().getPlaceBL();
		String blockName = e.getBlock().getType().name().toLowerCase();
		if(blacklist.isEmpty()) return;
		if(p.hasPermission("creativemanager.bypass.blacklist.place")) return;
		if(p.hasPermission("creativemanager.bypass.blacklist.place." + blockName)) return;
		if(SearchUtils.inList(blacklist, blockName)){
			HashMap<String, String> replaceMap = new HashMap<>();
			replaceMap.put("{BLOCK}", StringUtils.proper(e.getBlock().getType().name()));
			if (CreativeManager.getSettings().getBoolean("send-player-messages"))
				CreativeManager.sendMessage(p, CreativeManager.TAG + CreativeManager.getLang().getString("blacklist.place", replaceMap));
			e.setCancelled(true);
		}
	}
	/**
	 * On place.
	 *
	 * @param e the event.
	 */
	@EventHandler
	public void logBlock(BlockPlaceEvent e)
	{
		Player p = e.getPlayer();
		if(!p.getGameMode().equals(GameMode.CREATIVE)) return;
		if(p.hasPermission("creativemanager.bypass.logged")) return;
		if(!e.isCancelled()) plugin.getDataManager().addBlock(new BlockLog(e.getBlock(), e.getPlayer()));
	}
	/**
	 * On place.
	 *
	 * @param e the event.
	 */
	@EventHandler
	public void checkLog(BlockPlaceEvent e)
	{
		Player p = e.getPlayer();
		if(p.getGameMode().equals(GameMode.CREATIVE)) return;
		BlockLog blockLog = plugin.getDataManager().getBlockFrom(e.getBlock().getLocation());
		if (blockLog != null)
			if (blockLog.isCreative()) {
				plugin.getDataManager().removeBlock(blockLog.getLocation());
			}
	}
}
