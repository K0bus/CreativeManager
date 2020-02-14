package fr.k0bus.creativemanager.event;

import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.entity.ItemFrame;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;

import fr.k0bus.creativemanager.Main;

public class PlayerInteractEntity implements Listener {

	Main plugin;

	public PlayerInteractEntity(Main instance) {
		plugin = instance;
	}

	@EventHandler(ignoreCancelled = true)
	public void onUse(PlayerInteractEntityEvent e) {
		Player p = e.getPlayer();
		if(plugin.getConfig().getBoolean("entity-protection"))
		{
			if (e.getRightClicked() instanceof ItemFrame && p.getGameMode().equals(GameMode.CREATIVE)) {
				if (!p.hasPermission("creativemanager.entity")) {
					p.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("tag") + plugin.getLang().getString("permission.entity")));
					e.setCancelled(true);
				}
			}
		}
	}
}
