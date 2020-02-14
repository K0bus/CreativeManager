package fr.k0bus.creativemanager.event;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerGameModeChangeEvent;

import fr.k0bus.creativemanager.Main;
import fr.k0bus.creativemanager.manager.InventoryManager;

public class PlayerGamemodeChange implements Listener {

	Main plugin;

	public PlayerGamemodeChange(Main instance) {
		plugin = instance;
	}

	@EventHandler(ignoreCancelled = true)
	public void onGMChange(PlayerGameModeChangeEvent  e) {
		Player p = e.getPlayer();
		if(plugin.getConfig().getBoolean("creative-inventory") && !p.hasPermission("creativemanager.inventory"))
		{
			InventoryManager im = new InventoryManager(p, plugin);
			im.saveInventory(p.getGameMode());
			im.loadInventory(e.getNewGameMode());
			p.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("tag") + plugin.getLang().getString("inventory.change").replace("{GAMEMODE}", e.getNewGameMode().name())));
		}
	}
}
