package fr.k0bus.creativemanager.event;

import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;

import fr.k0bus.creativemanager.Main;

public class PlayerDrop implements Listener {

	Main plugin;

	public PlayerDrop(Main instance) {
		plugin = instance;
	}

	@EventHandler(ignoreCancelled = true)
	public void onDrop(PlayerDropItemEvent e) {
		Player p = e.getPlayer();
		if(plugin.getConfig().getBoolean("drop-protection") && p.getGameMode().equals(GameMode.CREATIVE))
		{
			if (!p.hasPermission("creativemanager.drop")) {
				p.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("tag") + plugin.getLang().getString("permission.drop")));
				e.setCancelled(true);
			}
		}
	}
}
