package fr.k0bus.creativemanager.event;

import fr.k0bus.creativemanager.CreativeManager;
import fr.k0bus.creativemanager.log.BlockLog;
import fr.k0bus.creativemanager.settings.Protections;
import fr.k0bus.creativemanager.utils.BlockUtils;
import fr.k0bus.creativemanager.utils.CMUtils;
import fr.k0bus.creativemanager.utils.SearchUtils;
import fr.k0bus.k0buscore.utils.StringUtils;
import org.bukkit.GameMode;
import org.bukkit.block.Block;
import org.bukkit.block.Container;
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
	@EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
	public void checkBuild(BlockPlaceEvent e) {
		Player p = e.getPlayer();
		if (p.getGameMode() == GameMode.CREATIVE) {
			if (CreativeManager.getSettings().getProtection(Protections.BUILD) && !p.hasPermission("creativemanager.bypass.build")) {
				if (CreativeManager.getSettings().getBoolean("send-player-messages"))
					CMUtils.sendMessage(p, "permission.build");
				e.setCancelled(true);
			}
			if (CreativeManager.getSettings().getProtection(Protections.BUILD_CONTAINER)  && !p.hasPermission("creativemanager.bypass.build-container"))
			{
				if(e.getBlock() instanceof Container container)
				{
					if (CreativeManager.getSettings().getBoolean("send-player-messages"))
						CMUtils.sendMessage(p, "permission.build-nbt");
					container.getInventory().clear();
					if(!container.getInventory().isEmpty())
						e.getBlock().setType(e.getBlock().getType());
				}
			}
		}
	}
	/**
	 * On place.
	 *
	 * @param e the event.
	 */
	@EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
	public void checkBlackList(BlockPlaceEvent e)
	{
		Player p = e.getPlayer();
		if(!p.getGameMode().equals(GameMode.CREATIVE)) return;
		List<String> blacklist = CreativeManager.getSettings().getPlaceBL();
		String blockName = e.getBlock().getType().name().toLowerCase();
		if(blacklist.isEmpty()) return;
		if(p.hasPermission("creativemanager.bypass.blacklist.place")) return;
		if(p.hasPermission("creativemanager.bypass.blacklist.place." + blockName)) return;
		if((CreativeManager.getSettings().getString("list.mode.place").equals("whitelist") && !SearchUtils.inList(blacklist, e.getBlock())) ||
			(!CreativeManager.getSettings().getString("list.mode.place").equals("whitelist") && SearchUtils.inList(blacklist, e.getBlock()))){
			HashMap<String, String> replaceMap = new HashMap<>();
			replaceMap.put("{BLOCK}", StringUtils.proper(e.getBlock().getType().name()));
			if (CreativeManager.getSettings().getBoolean("send-player-messages"))
				CMUtils.sendMessage(p, "blacklist.place", replaceMap);
			e.setCancelled(true);
		}
	}
	/**
	 * On place.
	 *
	 * @param e the event.
	 */
	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void logBlock(BlockPlaceEvent e)
	{
		Player p = e.getPlayer();
		if(!p.getGameMode().equals(GameMode.CREATIVE)) return;
		if(p.hasPermission("creativemanager.bypass.logged")) return;
		List<Block> blocks = BlockUtils.getBlockStructure(e.getBlock());
		for (Block block:blocks) {
			plugin.getDataManager().addBlock(new BlockLog(block, e.getPlayer()));
		}
	}
	/**
	 * On place.
	 *
	 * @param e the event.
	 */
	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
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
