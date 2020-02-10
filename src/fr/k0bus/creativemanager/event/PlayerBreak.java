package fr.k0bus.creativemanager.event;

import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

import fr.k0bus.creativemanager.Main;

public class PlayerBreak implements Listener{

	Main plugin;

	public PlayerBreak(Main instance)
	{
		plugin = instance;
	}

	@EventHandler(ignoreCancelled = true)
    public void onBreak(BlockBreakEvent e)
    {
		Player p = e.getPlayer();
		if(p.getGameMode() == GameMode.CREATIVE)
		{
			if(plugin.getConfig().getBoolean("build-protection") && !p.hasPermission("creativemanager.build"))
			{
				p.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("tag") + plugin.getConfig().getString("lang.permission.build")));
				e.setCancelled(true);
			}
		}

    }
}
