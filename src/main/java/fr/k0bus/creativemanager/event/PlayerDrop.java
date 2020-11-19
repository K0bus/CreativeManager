package fr.k0bus.creativemanager.event;

import fr.k0bus.creativemanager.settings.Protections;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;

import fr.k0bus.creativemanager.CreativeManager;

public class PlayerDrop implements Listener {

	CreativeManager plugin;

	public PlayerDrop(CreativeManager instance) {
		plugin = instance;
	}

	@EventHandler(ignoreCancelled = true)
	public void onDrop(PlayerDropItemEvent e) {
		Player p = e.getPlayer();
		if(plugin.getSettings().getProtection(Protections.DROP) && p.getGameMode().equals(GameMode.CREATIVE))
		{
			if (!p.hasPermission("creativemanager.bypass.drop")) {
				p.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getSettings().getTag() + plugin.getLang().getString("permission.drop")));
				e.setCancelled(true);
			}
		}
	}
}
