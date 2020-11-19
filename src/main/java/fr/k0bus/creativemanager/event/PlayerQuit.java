package fr.k0bus.creativemanager.event;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import fr.k0bus.creativemanager.CreativeManager;
import fr.k0bus.creativemanager.manager.InventoryManager;

public class PlayerQuit implements Listener {

	CreativeManager plugin;

	public PlayerQuit(CreativeManager instance) {
		plugin = instance;
	}

	@EventHandler(ignoreCancelled = true)
	public void onQuit(PlayerQuitEvent e) {
		Player p = e.getPlayer();
		if(plugin.getConfig().getBoolean("creative-inventory"))
		{
			InventoryManager im = new InventoryManager(p, plugin);
			im.saveInventory(p.getGameMode());
		}
	}
	@EventHandler(ignoreCancelled = true)
	public void onKicked(PlayerKickEvent e) {
		Player p = e.getPlayer();
		if(plugin.getConfig().getBoolean("creative-inventory"))
		{
			InventoryManager im = new InventoryManager(p, plugin);
			im.saveInventory(p.getGameMode());
		}
	}
}
