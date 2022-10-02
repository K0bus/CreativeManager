package fr.k0bus.creativemanager.event;

import fr.k0bus.creativemanager.CreativeManager;
import fr.k0bus.creativemanager.manager.InventoryManager;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerGameModeChangeEvent;

import java.util.HashMap;

/**
 * Player gamemode change event.
 */
public class PlayerGamemodeChange implements Listener {
	private final CreativeManager plugin;

	/**
	 * Instantiates a new Player gamemode change.
	 *
	 * @param instance the instance.
	 */
	public PlayerGamemodeChange(CreativeManager instance) {
		plugin = instance;
	}

	/**
	 * On gm change.
	 *
	 * @param e the event.
	 */
	@EventHandler(ignoreCancelled = true)
	public void onGMChange(PlayerGameModeChangeEvent e) {
		e.getPlayer().closeInventory();
		if(CreativeManager.getSettings().getBoolean("stop-inventory-save")) return;
		Player p = e.getPlayer();
		if (!p.hasPermission("creativemanager.bypass.inventory")) {
			InventoryManager im = new InventoryManager(p, plugin);
			if (!CreativeManager.getSettings().adventureInvEnable()) {
				if (e.getNewGameMode().equals(GameMode.ADVENTURE)) {
					im.saveInventory(p.getGameMode());
					return;
				} else if (p.getGameMode().equals(GameMode.ADVENTURE)) {
					im.saveInventory(GameMode.SURVIVAL);
					return;
				}
			}
			if (!CreativeManager.getSettings().creativeInvEnable()) {
				if (e.getNewGameMode().equals(GameMode.CREATIVE)) {
					im.saveInventory(p.getGameMode());
					return;
				} else if (p.getGameMode().equals(GameMode.CREATIVE)) {
					im.saveInventory(GameMode.SURVIVAL);
					return;
				}
			}
			if (!CreativeManager.getSettings().spectatorInvEnable()) {
				if (e.getNewGameMode().equals(GameMode.SPECTATOR)) {
					im.saveInventory(p.getGameMode());
					return;
				} else if (p.getGameMode().equals(GameMode.SPECTATOR)) {
					im.saveInventory(GameMode.SURVIVAL);
					return;
				}
			}
			im.saveInventory(p.getGameMode());
			im.loadInventory(e.getNewGameMode());
			HashMap<String, String> replaceMap = new HashMap<>();
			replaceMap.put("{GAMEMODE}", e.getNewGameMode().name());
			if(CreativeManager.getSettings().getBoolean("send-player-messages"))
				CreativeManager.sendMessage(p, CreativeManager.TAG + CreativeManager.getLang().getString("inventory.change", replaceMap));
		}
	}
}
