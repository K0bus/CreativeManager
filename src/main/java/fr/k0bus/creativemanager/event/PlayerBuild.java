package fr.k0bus.creativemanager.event;

import fr.k0bus.creativemanager.log.BlockLog;
import fr.k0bus.creativemanager.settings.Protections;
import fr.k0bus.k0buslib.utils.Messages;
import org.bukkit.GameMode;
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
				if(plugin.getSettings().getBoolean("send-player-messages"))
					Messages.sendMessage(plugin.getMessageManager(), p, "permission.build");
				e.setCancelled(true);
			}
			else if(plugin.getSettings().getPlaceBL().contains(e.getBlock().getType().name()) && !p.hasPermission("creativemanager.bypass.blacklist.place"))
			{
				HashMap<String, String> replaceMap = new HashMap<>();
				replaceMap.put("{BLOCK}", e.getBlock().getType().name());
				if(plugin.getSettings().getBoolean("send-player-messages"))
					Messages.sendMessage(plugin.getMessageManager(), p, "blacklist.place", replaceMap);
				e.setCancelled(true);
			}
			if(plugin.getSettings().getProtection(Protections.LOOT))
			{
				plugin.getDataManager().addBlock(new BlockLog(e.getBlock(), e.getPlayer()));
			}
		}
		else
		{
			if(plugin.getSettings().getProtection(Protections.LOOT))
			{
				BlockLog blockLog = plugin.getDataManager().getBlockFrom(e.getBlock().getLocation());
				if(blockLog != null)
					if(blockLog.isCreative())
					{
						plugin.getDataManager().removeBlock(blockLog.getLocation());
					}
			}
		}

    }
}
