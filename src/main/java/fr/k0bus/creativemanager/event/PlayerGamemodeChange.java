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
			if(e.getNewGameMode() == GameMode.ADVENTURE && !plugin.getConfig().getBoolean("adventure-inventory"))
			{
				if(p.getGameMode() == GameMode.CREATIVE && plugin.getConfig().getBoolean("creative-inventory"))
				{
					im.saveInventory(p.getGameMode());
					im.loadInventory(GameMode.SURVIVAL);
					p.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("tag") + plugin.getLang().getString("inventory.change").replace("{GAMEMODE}", e.getNewGameMode().name())));
				}
				return;
			}
			if(e.getNewGameMode() == GameMode.CREATIVE && !plugin.getConfig().getBoolean("creative-inventory"))
			{
				if(p.getGameMode() == GameMode.ADVENTURE && plugin.getConfig().getBoolean("adventure-inventory"))
				{
					im.saveInventory(p.getGameMode());
					im.loadInventory(GameMode.ADVENTURE);
					p.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("tag") + plugin.getLang().getString("inventory.change").replace("{GAMEMODE}", e.getNewGameMode().name())));
				}
				return;
			}
			if(e.getNewGameMode() == GameMode.SURVIVAL)
			{
				if(p.getGameMode() == GameMode.ADVENTURE && plugin.getConfig().getBoolean("adventure-inventory"))
				{
					im.saveInventory(p.getGameMode());
					im.loadInventory(GameMode.SURVIVAL);
					p.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("tag") + plugin.getLang().getString("inventory.change").replace("{GAMEMODE}", e.getNewGameMode().name())));
				}
				if(p.getGameMode() == GameMode.CREATIVE && plugin.getConfig().getBoolean("creative-inventory"))
				{
					im.saveInventory(p.getGameMode());
					im.loadInventory(GameMode.SURVIVAL);
					p.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("tag") + plugin.getLang().getString("inventory.change").replace("{GAMEMODE}", e.getNewGameMode().name())));
				}
				return;
			}
			im.saveInventory(p.getGameMode());
			im.loadInventory(e.getNewGameMode());
			p.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("tag") + plugin.getLang().getString("inventory.change").replace("{GAMEMODE}", e.getNewGameMode().name())));
		}
	}
}
