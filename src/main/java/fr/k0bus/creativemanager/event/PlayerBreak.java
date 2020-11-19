package fr.k0bus.creativemanager.event;

import fr.k0bus.creativemanager.settings.Protections;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

import fr.k0bus.creativemanager.CreativeManager;

public class PlayerBreak implements Listener{

	CreativeManager plugin;

	public PlayerBreak(CreativeManager instance)
	{
		plugin = instance;
	}

	@EventHandler(ignoreCancelled = true)
    public void onBreak(BlockBreakEvent e)
    {
		Player p = e.getPlayer();
		if(p.getGameMode() == GameMode.CREATIVE)
		{
			if(plugin.getSettings().getProtection(Protections.BUILD) && !p.hasPermission("creativemanager.bypass.build"))
			{
				p.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getSettings().getTag() + plugin.getLang().getString("permission.build")));
				e.setCancelled(true);
			}
		}

    }
}
