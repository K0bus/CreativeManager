package fr.k0bus.creativemanager.event;

import fr.k0bus.creativemanager.CreativeManager;
import fr.k0bus.creativemanager.log.BlockLog;
import fr.k0bus.creativemanager.settings.Protections;
import fr.k0bus.creativemanager.utils.SearchUtils;
import fr.k0bus.k0buscore.utils.StringUtils;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

import java.util.HashMap;
import java.util.List;

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
	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void onBreak(BlockBreakEvent e) {
		Player p = e.getPlayer();
		if(!p.getGameMode().equals(GameMode.CREATIVE)) return;
		if(!CreativeManager.getSettings().getProtection(Protections.BUILD)) return;
		if(p.hasPermission("creativemanager.bypass.build")) return;
		if (p.getGameMode() == GameMode.CREATIVE) {
			if (CreativeManager.getSettings().getBoolean("send-player-messages"))
				CreativeManager.sendMessage(p, CreativeManager.TAG + CreativeManager.getLang().getString("permission.build"));
			e.setCancelled(true);
		}
	}
	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void checkBlacklist(BlockBreakEvent e)
	{
		Player p = e.getPlayer();
		String blockName = e.getBlock().getType().name().toLowerCase();
		if(!p.getGameMode().equals(GameMode.CREATIVE)) return;
		if(p.hasPermission("creativemanager.bypass.blacklist.break")) return;
		if(p.hasPermission("creativemanager.bypass.blacklist.break." + blockName)) return;
		List<String> blacklist = CreativeManager.getSettings().getBreakBL();
		if((CreativeManager.getSettings().getString("list.mode.break").equals("whitelist") && !SearchUtils.inList(blacklist, blockName)) ||
				(!CreativeManager.getSettings().getString("list.mode.break").equals("whitelist") && SearchUtils.inList(blacklist, blockName))){
			HashMap<String, String> replaceMap = new HashMap<>();
			replaceMap.put("{BLOCK}", StringUtils.proper(e.getBlock().getType().name()));
			if (CreativeManager.getSettings().getBoolean("send-player-messages"))
				CreativeManager.sendMessage(p, CreativeManager.TAG + CreativeManager.getLang().getString("blacklist.place", replaceMap));
			e.setCancelled(true);
		}
	}

	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void checkLog(BlockBreakEvent e)
	{
		Player p = e.getPlayer();
		if(!p.getGameMode().equals(GameMode.CREATIVE)) {
			if (!p.hasPermission("creativemanager.bypass.break-creative"))
			{
				BlockLog blockLog = plugin.getDataManager().getBlockFrom(e.getBlock().getLocation());
				if (blockLog != null) {
					if (blockLog.isCreative()) {
						e.setCancelled(true);
						return;
					}
				}
			}
			if (p.hasPermission("creativemanager.bypass.log")) return;
			if (!CreativeManager.getSettings().getProtection(Protections.LOOT)) return;
			BlockLog blockLog = plugin.getDataManager().getBlockFrom(e.getBlock().getLocation());
			if (blockLog != null) {
				if (blockLog.isCreative()) {
					e.getBlock().setType(Material.AIR);
					plugin.getDataManager().removeBlock(blockLog.getLocation());
					e.setCancelled(true);
				}
			}
		}else {
			BlockLog blockLog = plugin.getDataManager().getBlockFrom(e.getBlock().getLocation());
			if (blockLog != null) {
				plugin.getDataManager().removeBlock(blockLog.getLocation());
			}
		}
	}
}
