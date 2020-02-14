package fr.k0bus.creativemanager.event;

import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;

import fr.k0bus.creativemanager.Main;

public class PlayerBuild  implements Listener{
	Main plugin;

	public PlayerBuild(Main instance)
	{
		plugin = instance;
	}

	@EventHandler(ignoreCancelled = true)
    public void onPlace(BlockPlaceEvent e)
    {
		Player p = e.getPlayer();
		if(p.getGameMode() == GameMode.CREATIVE)
		{
			if(plugin.getConfig().getBoolean("build-protection") && !p.hasPermission("creativemanager.build"))
			{
				p.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("tag") + plugin.getLang().getString("permission.build")));
				e.setCancelled(true);
			}
			else if(plugin.getConfig().getList("blacklist.place").contains(e.getBlock().getType().getKey().getKey()) && !p.hasPermission("creativemanager.bypass.blacklist.place"))
			{
				p.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("tag") + plugin.getLang().getString("blacklist.place").replace("{BLOCK}", e.getBlock().getType().getKey().getKey())));
				e.setCancelled(true);
			}
		}

    }
}
