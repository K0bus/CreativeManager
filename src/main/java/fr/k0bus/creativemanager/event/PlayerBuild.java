package fr.k0bus.creativemanager.event;

import fr.k0bus.creativemanager.log.BlockLog;
import fr.k0bus.creativemanager.settings.Protections;
import fr.k0bus.k0buslib.utils.Messages;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;

import fr.k0bus.creativemanager.CreativeManager;

import java.util.HashMap;

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
				Messages.sendMessage(plugin.getMessageManager(), p, "permission.build");
				e.setCancelled(true);
			}
			else if(plugin.getSettings().getPlaceBL().contains(e.getBlock().getType().name()) && !p.hasPermission("creativemanager.bypass.blacklist.place"))
			{
				HashMap<String, String> replaceMap = new HashMap<>();
				replaceMap.put("{BLOCK}", e.getBlock().getType().name());
				Messages.sendMessage(plugin.getMessageManager(), p, "blacklist.place", replaceMap);
				e.setCancelled(true);
			}
			if(plugin.getSettings().getProtection(Protections.LOOT))
			{
				Location location = e.getBlock().getLocation();
				if(plugin.getDataManager().getBlockBlockLogMap().containsKey(e.getBlock().getLocation()))
				{
					plugin.getDataManager().addToDelete(plugin.getDataManager().getBlockBlockLogMap().get(location).getUuid());
					plugin.getDataManager().removeLog(e.getBlock().getLocation());
				}
				plugin.getDataManager().addLog(new BlockLog(e.getBlock(), e.getPlayer()));
			}
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
