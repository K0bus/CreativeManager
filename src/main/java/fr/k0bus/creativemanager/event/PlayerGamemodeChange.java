package fr.k0bus.creativemanager.event;

import org.bukkit.ChatColor;
import org.bukkit.GameMode;
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
		if(!p.hasPermission("creativemanager.inventory"))
		{
			InventoryManager im = new InventoryManager(p, plugin);
			if(!plugin.getConfig().getBoolean("adventure-inventory"))
			{
				if(e.getNewGameMode().equals(GameMode.ADVENTURE))
				{
					im.saveInventory(p.getGameMode());
					return;
				}
				else if(p.getGameMode().equals(GameMode.ADVENTURE))
				{
					im.saveInventory(GameMode.SURVIVAL);
					return;
				}
			}
			if(!plugin.getConfig().getBoolean("creative-inventory"))
			{
				if(e.getNewGameMode().equals(GameMode.CREATIVE))
				{
					im.saveInventory(p.getGameMode());
					return;
				}
				else if(p.getGameMode().equals(GameMode.CREATIVE))
				{
					im.saveInventory(GameMode.SURVIVAL);
					return;
				}
			}
			im.saveInventory(p.getGameMode());
			im.loadInventory(e.getNewGameMode());
			p.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("tag") + plugin.getLang().getString("inventory.change").replace("{GAMEMODE}", e.getNewGameMode().name())));
		}
	}
}
