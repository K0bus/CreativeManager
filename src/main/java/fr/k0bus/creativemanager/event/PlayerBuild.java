package fr.k0bus.creativemanager.event;

import fr.k0bus.creativemanager.log.BlockLog;
import fr.k0bus.creativemanager.settings.Protections;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;

import fr.k0bus.creativemanager.CreativeManager;

public class PlayerBuild  implements Listener{
	CreativeManager plugin;

	public PlayerBuild(CreativeManager instance)
	{
		plugin = instance;
	}

	@EventHandler(ignoreCancelled = true)
    public void onPlace(BlockPlaceEvent e)
    {
		Player p = e.getPlayer();
		if(p.getGameMode() == GameMode.CREATIVE)
		{
			if(plugin.getSettings().getProtection(Protections.BUILD) && !p.hasPermission("creativemanager.bypass.build"))
			{
				p.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getSettings().getTag() + plugin.getLang().getString("permission.build")));
				e.setCancelled(true);
			}
			else if(plugin.getSettings().getPlaceBL().contains(e.getBlock().getType().name()) && !p.hasPermission("creativemanager.bypass.blacklist.place"))
			{
				p.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getSettings().getTag() + plugin.getLang().getString("blacklist.place").replace("{BLOCK}", e.getBlock().getType().name())));
				e.setCancelled(true);
			}
			if(plugin.getSettings().getProtection(Protections.LOOT))
				new BlockLog(e.getBlock(), e.getPlayer()).save();
		}
		else
		{
			if(plugin.getSettings().getProtection(Protections.LOOT))
			{
				BlockLog blockLog = new BlockLog(e.getBlock());
				if(blockLog.isCreative())
				{
					blockLog.delete();
				}
			}
		}

    }
}
